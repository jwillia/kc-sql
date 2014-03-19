package com.rsmart.kuali.coeus.hr.rest.authn;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

public class KCBasicAuthFilter implements Filter {

  private static final String   IMPORT_AUTHN_USER = "hrimport.authn.username";
  private static final String   IMPORT_AUTHN_PASS = "hrimport.authn.password";
  private static final String   IMPORT_AUTHN_RUN_AS = "hrimport.authn.runas";
  private static final String   AUTH_HEADER = "Authorization";

  protected BusinessObjectService   businessObjectService = null;
  protected IdentityService         identityService = null;
  protected HashSet<String>         authorizedUsers = null;
  protected String                  username = null;
  protected String                  password = null;
  protected String                  runAs = null;
  
  public BusinessObjectService getBusinessObjectService() {
    if (businessObjectService == null) {
      businessObjectService = KRADServiceLocator.getBusinessObjectService();
    }

    return businessObjectService;
  }

  public void setBusinessObjectService(BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

  public IdentityService getIdentityService() {
    if (identityService == null) {
      identityService = KimApiServiceLocator.getIdentityService();
    }
    
    return identityService;
  }

  public void setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
  }

  protected String getUsername() {
    return ConfigContext.getCurrentContextConfig().getProperty(IMPORT_AUTHN_USER);
  }
  
  protected String getPassword() {
    return ConfigContext.getCurrentContextConfig().getProperty(IMPORT_AUTHN_PASS);
  }
  
  protected String getRunAsUser() {
    return ConfigContext.getCurrentContextConfig().getProperty(IMPORT_AUTHN_RUN_AS);
  }
  
  protected UserSession authenticateKCUser(final String principalName, final String password) 
      throws GeneralSecurityException {
    PrincipalContract principal = null;
    
    final String username = getUsername();
    final String fixedPassword = getPassword();
    final String runas = getRunAsUser();
    
    if (username != null && fixedPassword != null) {
      if (runas == null) {
        error("no runas user set! Fixed credentials cannot be used without it. Please configure " + IMPORT_AUTHN_RUN_AS 
            + " to indicate the valid KIM user to use for import");
      } else if (username.equals(principalName) && fixedPassword.equals(password)) {
        debug ("user authenticated against fixed username and password");
        principal = getIdentityService().getPrincipalByPrincipalName(runas);
        if (principal == null) {
          error ("could not retrieve runas user '" + runas + "' -- user cannot authenitcate!");
          return null;
        }
      } else {
        return null;
      }
    } else {
      debug ("no fixed username and password configured: authenticating against KIM\n configure " + IMPORT_AUTHN_USER +
          ", " + IMPORT_AUTHN_PASS + ", and " + IMPORT_AUTHN_RUN_AS + " to authentication against fixed credentials");
    }
    
    if (principal == null) {
      if (authorizedUsers != null) {
        if (!authorizedUsers.contains(principalName)) {
          debug(principalName + " is not in the authorized users list: aborting authentication");
          return null;
        }
      }
      
      String convertedPw = null;
      
      convertedPw = CoreApiServiceLocator.getEncryptionService().hash(password);
      
      principal = getIdentityService().getPrincipalByPrincipalNameAndPassword(principalName, convertedPw);
    }
      
    if (principal != null) {
      final UserSession userSession = new UserSession(principal.getPrincipalName());
      GlobalVariables.setUserSession(userSession);
      return userSession;
    }

    debug("unable to retrieve user " + principalName + " with the supplied password");
    
    return null;
  }
  
  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response,
      final FilterChain filterChain) throws IOException, ServletException {
    final HttpServletRequest httpReq = (HttpServletRequest)request;
    final String authHeader = httpReq.getHeader(AUTH_HEADER);
    
    UserSession session = null;
    
    if (authHeader != null) {
      final String[] parts = authHeader.split("\\s+");
      if (parts.length == 2 && "basic".equalsIgnoreCase(parts[0])) {
        final String plaintext = new String(DatatypeConverter.parseBase64Binary(parts[1]));
        if (plaintext != null && plaintext.length() > 0) {
          final String[] credentials = plaintext.split(":");
          try {
            session = authenticateKCUser(credentials[0], credentials[1]);
          } catch (GeneralSecurityException e) {
            error("security exception encountered during authentication", e);
            final HttpServletResponse httpResp = (HttpServletResponse)response;
            httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
          }
        }
      }
    }
    
    if (session == null) {
      debug ("user is not authenticated");
      final HttpServletResponse httpResp = (HttpServletResponse)response;
      httpResp.setHeader("WWW-Authentication", "Basic realm=\"" + httpReq.getRequestURI() + "\"");
      httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } else {
      debug("authenticated user: '" + session.getPrincipalName() + "'");
      filterChain.doFilter(request, response);
    }
  }

  @Override
  public void init(final FilterConfig config) throws ServletException {
    final String hrUsers = config.getInitParameter("hrUsers");
    if (hrUsers != null && !hrUsers.isEmpty()) {
      final String users[] = hrUsers.split(",");
      if(users.length > 0) {
        final StringBuffer sb = new StringBuffer("HR import authorized users resricted to: ");
        String delim = "";
        authorizedUsers = new HashSet<String> (users.length);
        for (final String user : users) {
          sb.append(delim).append(user);
          delim = ",";
          authorizedUsers.add(user);
        }
        debug(sb.toString());
      }
    }
  }

  @Override
  public void destroy() {
  }

}

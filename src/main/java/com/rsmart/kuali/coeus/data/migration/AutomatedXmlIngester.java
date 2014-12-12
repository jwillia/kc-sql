package com.rsmart.kuali.coeus.data.migration;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.impex.xml.CompositeXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.FileXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;
import org.kuali.rice.core.api.impex.xml.ZipXmlDocCollection;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Automatically ingest files from XML on startup. Inspired by and mostly stolen from
 * {@link org.kuali.rice.core.web.impex.IngesterAction}.
 */
public class AutomatedXmlIngester implements SmartApplicationListener {
  private static final Logger LOG = Logger.getLogger(AutomatedXmlIngester.class);
  protected IdentityService identityService = null;
  protected XmlIngesterService xmlIngesterService = null;

  // TODO make this configurable in kc-config.xml
  final String principalName = "admin";
  final String discoveryPath = "classpath*:com/rsmart/kuali/coeus/data/migration/xml/*.*";

  public void init() {
    return;
  }

  public void doIt() {
    LOG.info("Searching for resources to ingest in path: " + discoveryPath);
    final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Collection<File> files = null;
    try {
      final Resource[] resources = resolver.getResources(discoveryPath);
      LOG.info("Resolver found the following resources: " + resources);
      files = new ArrayList<File>(resources.length);
      for (final Resource resource : resources) {
        if (resource.getURI().toString().startsWith("file:")) {
          LOG.info("Adding resource for XML ingestion: " + resource.getFilename() + " : "
              + resource.contentLength() + " bytes");
          files.add(resource.getFile());
        } else {
          LOG.warn("Resource NOT currently supported!: " + resource.getURI());
        }
      }
    } catch (IOException e) {
      LOG.error(e.getMessage() + ":\n" + ExceptionUtils.getFullStackTrace(e));
    }
    ingest(files);
  }

  public void ingest(final Collection<File> files) {
    final Principal principal = identityService
        .getPrincipalByPrincipalName(principalName);
    if (principal == null) {
      throw new IllegalStateException("Could not find Principal: " + principalName);
    }

    final List<XmlDocCollection> collections = new ArrayList<XmlDocCollection>(
        files.size());
    for (final File file : files) {
      final String fileName = file.getName();
      if (fileName.toLowerCase().endsWith(".zip")) {
        try {
          collections.add(new ZipXmlDocCollection(file));
        } catch (IOException e) {
          LOG.error("Unable to load file: " + file);
          throw new Error(e);
        }
      } else if (fileName.toLowerCase().endsWith(".xml")) {
        collections.add(new FileXmlDocCollection(file, fileName));
      } else {
        LOG.warn("Ignoring extraneous file: " + fileName);
      }
    }
    if (collections.isEmpty()) {
      LOG.error("No valid files to ingest");
    } else {
      // wrap in composite collection to make transactional
      final CompositeXmlDocCollection compositeCollection = new CompositeXmlDocCollection(
          collections);
      int totalProcessed = 0;
      final List<XmlDocCollection> c = new ArrayList<XmlDocCollection>(1);
      c.add(compositeCollection);
      try {
        final Collection<XmlDocCollection> failed = xmlIngesterService.ingest(c,
            principal.getPrincipalId());
        final boolean txFailed = failed.size() > 0;
        if (txFailed) {
          LOG.error("Ingestion failed");
        }
        for (final XmlDocCollection collection : collections) {
          final List<? extends XmlDoc> docs = collection.getXmlDocs();
          for (final XmlDoc doc : docs) {
            if (doc.isProcessed()) {
              if (!txFailed) {
                totalProcessed++;
                LOG.info("Ingested xml doc: "
                    + doc.getName()
                    + (doc.getProcessingMessage() == null ? "" : "\n"
                        + doc.getProcessingMessage()));
              } else {
                LOG.info("Rolled back doc: "
                    + doc.getName()
                    + (doc.getProcessingMessage() == null ? "" : "\n"
                        + doc.getProcessingMessage()));
              }
            } else {
              LOG.error("Failed to ingest xml doc: "
                  + doc.getName()
                  + (doc.getProcessingMessage() == null ? "" : "\n"
                      + doc.getProcessingMessage()));
            }
          }
        }
      } catch (Exception e) {
        LOG.error("Error during ingest: " + e.getMessage() + ":\n"
            + ExceptionUtils.getFullStackTrace(e));
      }
      if (totalProcessed == 0) {
        LOG.info("No xml docs ingested!");
      }
    }
  }

  public void setIdentityService(final IdentityService identityService) {
    this.identityService = identityService;
  }

  public void setXmlIngesterService(final XmlIngesterService xmlIngesterService) {
    this.xmlIngesterService = xmlIngesterService;
  }

  @Override
  public void onApplicationEvent(final ApplicationEvent applicationEvent) {
    LOG.info("onApplicationEvent(final ApplicationEvent " + applicationEvent + ")");
    if (applicationEvent instanceof ContextRefreshedEvent) {
      doIt();
    }
  }

  @Override
  public int getOrder() {
    return SmartApplicationListener.LOWEST_PRECEDENCE;
  }

  @Override
  public boolean supportsEventType(Class<? extends ApplicationEvent> arg0) {
    return true;
  }

  @Override
  public boolean supportsSourceType(Class<?> arg0) {
    return true;
  }

  public IdentityService getIdentityService() {
    if (identityService == null) {
      identityService = getService("kimIdentityService");
    }
    return identityService;
  }

  public XmlIngesterService getXmlIngesterService() {
    if (xmlIngesterService == null) {
      xmlIngesterService = getService("xmlIngesterService");
    }
    return xmlIngesterService;
  }

  private <T> T getService(final String serviceName) {
    return GlobalResourceLoader.<T> getService(serviceName);
  }
}

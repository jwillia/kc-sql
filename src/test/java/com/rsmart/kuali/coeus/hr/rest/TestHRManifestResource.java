package com.rsmart.kuali.coeus.hr.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import java.io.InputStream;

import javax.ws.rs.core.Response;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestHRManifestResource {

  @Mock
  private HRManifestService importService;

  @Test
  public void testImportHRManifest() throws Exception {
    HRManifestResource resource = new HRManifestResource();
    resource.setManifestService(importService);

    InputStream manifestStream = getClass().getResourceAsStream("/exampleimport.xml");
    Response response = resource.processManifest(manifestStream);

    verify(importService).importHRManifest(argThat(new HRManifestArgumentMatcher()));
    assertEquals(200, response.getStatus());
  }

  class HRManifestArgumentMatcher extends ArgumentMatcher<HRManifest> {
    public boolean matches(Object o) {
      if (o instanceof HRManifest) {
        HRManifest manifest = (HRManifest) o;

        try {
          TestHRManifest.validateTestHRManifest(manifest);
        } catch (Exception e) {
          throw new RuntimeException(
              "propogating checked exception through ArgumentMatcher interface", e);
        }

        return true;
      }
      return false;
    }
  }
}

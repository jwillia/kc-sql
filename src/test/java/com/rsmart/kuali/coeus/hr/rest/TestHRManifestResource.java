package com.rsmart.kuali.coeus.hr.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

public class TestHRManifestResource {

	@Test
	public void testMissingParamsReportsError() throws Exception {
		final HRManifestResource resource = new HRManifestResource();
		final Response resp = resource.processManifest(null);
		  
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
	}

}

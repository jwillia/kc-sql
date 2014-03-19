package com.rsmart.kuali.coeus.hr.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Test;

public class TestHRImportResource {

	@Test
	public void testMissingParamsReportsError() throws Exception {
		final HRImportResource resource = new HRImportResource();
		final Response resp = resource.processImport(null);
		  
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
	}

}

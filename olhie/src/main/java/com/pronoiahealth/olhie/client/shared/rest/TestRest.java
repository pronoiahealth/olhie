package com.pronoiahealth.olhie.client.shared.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/test")
public interface TestRest {

	@POST
	@Path("/fetch")
	@Consumes("application/json")
	@Produces("application/json")
	public String getTest();
}

package com.yetu.emscher.app.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestResource {
	
	@Inject
	public TestResource() {
		
	}
	
	@GET
	public String getTest() {
		return "Test successfull";
	}

}

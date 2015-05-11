package com.yetu.emscher.app.resources;

import java.time.LocalTime;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yetu.emscher.app.UpdateRepository;
import com.yetu.omaha.App;
import com.yetu.omaha.request.Request;
import com.yetu.omaha.response.Daystart;
import com.yetu.omaha.response.Response;

@Path("/update")
public class UpdateResource {

	UpdateRepository updateRepo;

	@Inject
	public UpdateResource(UpdateRepository updateRepo) {
		this.updateRepo = updateRepo;
	}

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response call(Request request) {
		// TODO check the request
		Response response = new Response();
		App updatedApp = updateRepo.getUpdateForVersion(request.getApp());
		response.setApp(updatedApp);
		response.setDaystart(getDaystart());
		return response;
	}
	
	private Daystart getDaystart(){
		LocalTime time = LocalTime.now();
		int secondsSinceMidnight = time.toSecondOfDay();
		Daystart daystart = new Daystart();
		daystart.setElapsed_seconds(secondsSinceMidnight);
		return daystart;
	}

}

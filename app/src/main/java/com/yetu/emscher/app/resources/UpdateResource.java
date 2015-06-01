package com.yetu.emscher.app.resources;

import io.dropwizard.jersey.caching.CacheControl;

import java.time.LocalTime;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.request.Event;
import com.yetu.omaha.request.Request;
import com.yetu.omaha.response.Daystart;
import com.yetu.omaha.response.Response;

@Path("/update")
public class UpdateResource {

	private final static Logger logger = LoggerFactory
			.getLogger(UpdateResource.class);

	UpdateRepository updateRepo;

	@Inject
	public UpdateResource(UpdateRepository updateRepo) {
		this.updateRepo = updateRepo;
		if (updateRepo == null) {
			logger.error("DI didn't work as expected");
		} else {
			logger.debug("Implementation of UpdateRepository is {}", updateRepo
					.getClass().getCanonicalName());
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_XML)
	@CacheControl(noCache = true)
	@Metered(name = "update-resource")
	public Response call(Request request) {
		logger.debug("Received request for update endpoint");
		// TODO check the request
		Response response = new Response();
		response.setProtocol("3.0");
		if (isUpdateRequest(request)) {
			logger.debug("Client with version {} looking for an update",
					request.getApp().getVersion());
			App updatedApp = updateRepo.getUpdateForVersion(request.getApp());
			logger.debug(
					"Sending update instructions to get him to version {}",
					updatedApp.getVersion());
			response.setApp(updatedApp);
		} else {
			App responseApp = new App();
			responseApp.setAppid(request.getApp().getAppid());
			responseApp.setStatus("ok");

			Ping ping = new Ping();
			ping.setStatus("ok");

			Event event = new Event();
			event.setStatus("ok");

			responseApp.setPing(ping);
			responseApp.setEvent(event);

			response.setApp(responseApp);
		}
		response.setDaystart(getDaystart());
		return response;
	}

	private boolean isUpdateRequest(Request request) {
		return request.getApp().getUpdatecheck() != null;
	}

	private Daystart getDaystart() {
		LocalTime time = LocalTime.now();
		int secondsSinceMidnight = time.toSecondOfDay();
		Daystart daystart = new Daystart();
		daystart.setElapsed_seconds(secondsSinceMidnight);
		return daystart;
	}

}

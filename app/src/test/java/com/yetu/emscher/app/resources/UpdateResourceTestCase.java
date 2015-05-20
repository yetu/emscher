package com.yetu.emscher.app.resources;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.fakerepo.FakeUpdateRepo;
import com.yetu.omaha.request.Request;
import com.yetu.omaha.response.Response;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class UpdateResourceTestCase {

	@Module(injects = { UpdateResource.class, FakeUpdateRepo.class }, complete = true, library = false)
	public static class TestCaseModule {

		@Provides
		public UpdateRepository provideUpdateRepo() {
			return new FakeUpdateRepo();
		}
	}

	private UpdateResource resource;

	@Before
	public void setup() {
		ObjectGraph graph = ObjectGraph.create(new TestCaseModule());
		resource = graph.get(UpdateResource.class);
	}

	private Request readRequest(String filename) throws JsonParseException,
			JsonMappingException, IOException {
		XmlMapper mapper = new XmlMapper();
		Request request = mapper.readValue(new File(filename), Request.class);
		return request;
	}

	@Test
	public void testPingResponse() throws Exception {
		Request request = readRequest("src/test/resources/update-ping.request");

		Response response = resource.call(request);
		assertNotNull(response);
		assertNotNull(response.getDaystart());
		assertNotNull(response.getApp());
		assertNotNull(response.getApp().getPing());
		assertNotNull(response.getApp().getEvent());
		assertNull(response.getApp().getUpdatecheck());
	}

	@Test
	public void testUpdateCheckRequest() throws Exception {
		Request request = readRequest("src/test/resources/updatecheck.request");

		Response response = resource.call(request);

		assertNotNull(response);
		assertNotNull(response.getDaystart());
		assertNotNull(response.getApp());
		assertNotNull(response.getApp().getPing());
		assertNotNull(response.getApp().getUpdatecheck());
	}

}

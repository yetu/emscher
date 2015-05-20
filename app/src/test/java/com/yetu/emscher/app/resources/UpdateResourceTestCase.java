package com.yetu.emscher.app.resources;

import java.io.File;

import static org.junit.Assert.*;
import org.junit.Test;

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

	@Test
	public void testPingResponse() throws Exception {
		ObjectGraph graph = ObjectGraph.create(new TestCaseModule());
		UpdateResource resource = graph.get(UpdateResource.class);

		XmlMapper mapper = new XmlMapper();
		Request request = mapper.readValue(new File(
				"src/test/resources/update-ping.request"), Request.class);

		Response response = resource.call(request);
		assertNotNull(response);
		assertNotNull(response.getDaystart());
		assertNotNull(response.getApp());
		assertNotNull(response.getApp().getPing());
		assertNotNull(response.getApp().getEvent());
		assertNull(response.getApp().getUpdatecheck());
	}

}

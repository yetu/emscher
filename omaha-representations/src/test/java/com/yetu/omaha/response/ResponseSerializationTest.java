package com.yetu.omaha.response;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature;

public class ResponseSerializationTest {
	
	private final static String expectedResponse = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<response protocol=\"3.0\">"
			+ "<daystart elapsed_seconds=\"3000\">"
			+ "<app appid=\"yetu os\" status=\"ok\">"
			+ "<ping status=\"ok\"/>"
			+ "<updatecheck status=\"ok\">"
			+ "<urls>"
			+ "<url codebase=\"http://update.yetu.me/static\"/>"
			+ "</urls>"
			+ "<manifest version=\"9999.0.0\">"
			+ "<packages>"
            + "<package hash=\"some cool hash\" name=\"update.gz\" size=\"12000\" required=\"true\"/>"
            + "</packages>"
            + "<actions>"
            + "<action event=\"postinstall\""
            + " sha256=\"some cool hash\""
            + " needsadmin=\"false\""
            + " ChromeOSVersion=\"9999.0.0\""
            + " IsDeltaPayload=\"false\""
            + "/>"
            + "</actions>"
            + "</manifest>"
            + "</updatecheck>"
            + "</app>"
            + "</response>";
	
	//FIXME need to validate the format some other way
	@Test
	public void testSerializationToXml() throws Exception {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable(Feature.WRITE_XML_DECLARATION);
		
		Action action = new Action();
		action.setChromeOSVersion("9999.0.0");
		action.setEvent("postinstall");
		action.setIsDeltaPayload(false);
		action.setNeedsadmin(false);
		action.setSha256("some cool hash");
		
		Package pkg = new Package();
		pkg.setHash("some cool hash");
		pkg.setName("update.gz");
		pkg.setRequired(true);
		pkg.setSize(12000);
		
		Manifest manifest = new Manifest();
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(action);
		ArrayList<Package> packages = new ArrayList<Package>();
		packages.add(pkg);
		manifest.setActions(actions);
		manifest.setPackages(packages);
		manifest.setVersion("9999.0.0");
		
		Url url = new Url();
		url.setCodebase("http://update.yetu.me/static");
		ArrayList<Url> urls = new ArrayList<Url>();
		urls.add(url);
		
		UpdateCheck updatecheck = new UpdateCheck();
		updatecheck.setManifest(manifest);
		updatecheck.setStatus("ok");
		updatecheck.setUrls(urls);
		
		Ping ping = new Ping();
		ping.setStatus("ok");
		
		App app = new App();
		app.setAppid("yetu os");
		app.setPing(ping);
		app.setStatus("ok");
		app.setUpdatecheck(updatecheck);
		
		Response response = new Response();
		response.setProtocol("3.0");
		Daystart daystart = new Daystart();
		daystart.setElapsed_seconds(3000);
		response.setDaystart(daystart);
		response.setApp(app);
		
		String xml = xmlMapper.writeValueAsString(response);
		String expected = expectedResponse;
		Assert.assertEquals(expectedResponse, xml);
	}

}

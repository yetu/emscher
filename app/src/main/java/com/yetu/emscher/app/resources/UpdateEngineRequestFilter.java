package com.yetu.emscher.app.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreMatching
@Provider
public class UpdateEngineRequestFilter implements ContainerRequestFilter {

	private final static Logger logger = LoggerFactory
			.getLogger(UpdateEngineRequestFilter.class);

	private final static String HEADER_FIELD_CONTENT_TYPE = "Content-Type";
	private final static String HEADER_FIELD_CONTENT_ENCODING = "Content-Encoding";

	private final static Map<String, String> DEFAULT_VALUES = new HashMap<String, String>();
	static {
		DEFAULT_VALUES.put(HEADER_FIELD_CONTENT_TYPE, "application/xml");
		DEFAULT_VALUES.put(HEADER_FIELD_CONTENT_ENCODING, "identity");
	}

	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		logger.debug("Received request on {}", requestContext.getUriInfo()
				.getPath());
		if (requestContext.getUriInfo().getPath().startsWith("update")) {
			requestContext.getHeaders().put(
					HEADER_FIELD_CONTENT_TYPE,
					Arrays.asList(new String[] { DEFAULT_VALUES
							.get(HEADER_FIELD_CONTENT_TYPE) }));
			logger.debug("Received content type {}",
					requestContext.getHeaderString(HEADER_FIELD_CONTENT_TYPE));
			for (String key : DEFAULT_VALUES.keySet()) {
				String receivedValue = requestContext.getHeaderString(key);
				if (StringUtils.isEmpty(receivedValue)
						|| "null".equals(receivedValue)) {
					logger.debug("Adding missing header value {} for field {}",
							DEFAULT_VALUES.get(key), key);
					requestContext.getHeaders().put(
							key,
							Arrays.asList(new String[] { DEFAULT_VALUES
									.get(key) }));
				}
			}
		}

	}
}

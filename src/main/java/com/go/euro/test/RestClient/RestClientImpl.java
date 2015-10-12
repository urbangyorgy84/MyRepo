package com.go.euro.test.RestClient;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientImpl implements RestClient {
	private final static Logger LOGGER = Logger.getLogger(RestClientImpl.class);

	protected static final String END_POINT = "http://api.goeuro.com/api/v2/position/suggest/hun/";
	protected static Client client = ClientBuilder.newClient();

	/**
	 * get the JsonNode from the endpoint 
	 * @param city name of the city , last part of the endpoint
	 * @return given json node
	 * @throws IOException 
	 * */
	public JsonNode getJsonFromUri(String city)  {
		client.property(ClientProperties.CONNECT_TIMEOUT, 2000);
	    client.property(ClientProperties.READ_TIMEOUT,    2000);
	    String responseMsg  = "";

	    WebTarget target = client.target(END_POINT+city);
	    try {
	    	responseMsg = target.request().get(String.class);
		} catch (Exception e) {
	    	LOGGER.error("Error during the request!: ", e);
		}
	    if(responseMsg != null && !responseMsg.isEmpty() && !"[]".equals(responseMsg)){
		    return createJsonFromString(responseMsg);
	    }else{
	    	LOGGER.error("The requested City is not exist in the database");
	    	return null;
	    }
	}

	protected JsonNode createJsonFromString (String jsonStr) {
		if(jsonStr.isEmpty()){
	    	LOGGER.error("Given Json cannot be empty string!");
	    	return null;	
		}
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = null;
	    try {
			 actualObj = mapper.readTree(jsonStr);
		} catch (JsonProcessingException e) {
	    	LOGGER.error("Error during the Json Processing!: ", e);
		} catch (IOException e) {
	    	LOGGER.error("Error during the IO operation!: ", e);
		}
	    return actualObj;
	}
}

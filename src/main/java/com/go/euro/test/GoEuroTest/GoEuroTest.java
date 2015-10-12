package com.go.euro.test.GoEuroTest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.go.euro.test.JsonHandler.JsonHandler;
import com.go.euro.test.JsonHandler.JsonHandlerImpl;
import com.go.euro.test.RestClient.RestClient;
import com.go.euro.test.RestClient.RestClientImpl;

public class GoEuroTest 
{
	private final static Logger LOGGER = Logger.getLogger(GoEuroTest.class);
	
    public static void main( String[] args ) throws Exception
    {
    	if(args.length < 1 || args[0].isEmpty()){
	    	LOGGER.error("This app should start with a city parameter");
	    	return;
    	}
    	RestClient client = new RestClientImpl();
    	JsonHandler jsonHandler = new JsonHandlerImpl();
    	String city =args[0] ;
    	
    	JsonNode jsonResponse = client.getJsonFromUri(city);
    	if(jsonResponse != null){
        	ArrayNode alignedNodes = jsonHandler.validateAndTransformJsonToCorrectFormat(jsonResponse);
        	if(alignedNodes != null){
            	jsonHandler.createCsvFileFromJson(alignedNodes, city);
        	}
    	}
    }
}

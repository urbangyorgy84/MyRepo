package com.go.euro.test.JsonHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public interface JsonHandler {
	
	void createCsvFileFromJson(JsonNode node, String city) ;
	
	ArrayNode validateAndTransformJsonToCorrectFormat(JsonNode node);
}

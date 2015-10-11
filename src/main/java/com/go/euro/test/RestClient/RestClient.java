package com.go.euro.test.RestClient;


import com.fasterxml.jackson.databind.JsonNode;

public interface RestClient {
	JsonNode getJsonFromUri( String city);
}

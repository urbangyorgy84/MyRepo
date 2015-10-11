package com.go.euro.test.RestClient;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fge.jackson.JsonLoader;

public class RestClientImplTest {

	@Test
	public void testRestClientImpl() throws IOException{
		RestClientTester tester= new RestClientTester(readRecources("schemas/correctJson.json"));
		String actual = tester.getJsonFromUri("Belrin").toString();
		String expected = JsonLoader.fromResource("/schemas/correctJson.json").toString();
		Assert.assertEquals(expected, actual);;
	}

	private class RestClientTester extends RestClientImpl {

		public RestClientTester(String json) {
			Client mockedClient = Mockito.mock(Client.class);
			WebTarget mockedWebTarget = Mockito.mock(WebTarget.class);
			Invocation.Builder mockedIb = Mockito
					.mock(Invocation.Builder.class);

			Mockito.stub(mockedIb.get(String.class)).toReturn(json);
			Mockito.stub(mockedWebTarget.request()).toReturn(mockedIb);
			Mockito.stub(mockedClient.target(Mockito.anyString())).toReturn(
					mockedWebTarget);
			client = mockedClient;
		}
	}

	private String readRecources(String expectedcontentFileName)
			throws IOException {
		String expectedContent = "";
		try {
			expectedContent = IOUtils.toString(getClass().getClassLoader()
					.getResourceAsStream(expectedcontentFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedContent;
	}
}

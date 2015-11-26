package com.go.euro.test.JsonHandler;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.go.euro.test.Utility.UtilityTest;

public class JsonHandlerImplTest {

	@Test
	public void TestJsonHandlerImplWithCorrect() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		JsonNode preparedNode = tester
				.validateAndTransformJsonToCorrectFormat(JsonLoader
						.fromResource("/schemas/correctJson.json"));
		tester.createCsvFileFromJson(preparedNode, "Berlin");
		String actual = UtilityTest.readFile("Berlin.csv");
		String expected = UtilityTest.readRecources("csvFiles/correct.csv");
		Assert.assertEquals(expected, actual);
		Assert.assertTrue("The file is not deleted", UtilityTest.removeFile("Berlin.csv"));;
	}

	@Test
	public void TestJsonHandlerImplWithCorrectNullAllowed() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		JsonNode preparedNode = tester.validateAndTransformJsonToCorrectFormat(
				JsonLoader.fromResource("/schemas/correctJsonWithNullValue.json"));
		tester.createCsvFileFromJson(preparedNode, "Berlin");
		
		String actual = UtilityTest.readFile("Berlin.csv");
		String expected = UtilityTest.readRecources("csvFiles/correctCsvWithNullValue.csv");
		
		Assert.assertEquals(expected, actual);
		Assert.assertTrue("The file is not deleted", UtilityTest.removeFile("Berlin.csv"));;
	}

	@Test
	public void TestJsonHandlerImplWithMissingField() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		Assert.assertNull(tester.validateAndTransformJsonToCorrectFormat(
						JsonLoader.fromResource("/schemas/missingMandatoryField.json")));
	}

	@Test
	public void TestJsonHandlerImplWithBaType() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		Assert.assertNull(tester.validateAndTransformJsonToCorrectFormat(
				JsonLoader.fromResource("/schemas/mandatoryFieldWithBadType.json")));
	}

}

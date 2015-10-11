package com.go.euro.test.JsonHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

public class JsonHandlerImplTest {

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	@Test
	public void TestJsonHandlerImplWithCorrect() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		JsonNode preparedNode = tester
				.validateAndTransformJsonToCorrectFormat(JsonLoader
						.fromResource("/schemas/correctJson.json"));
		tester.createCsvFileFromJson(preparedNode, "Berlin");
		String actual = readFile("Berlin.csv");
		String expected = readRecources("csvFiles/correct.csv");
		Assert.assertEquals(expected, actual);
		Assert.assertTrue("The file is not deleted", removeFile("Berlin.csv"));;
	}

	@Test
	public void TestJsonHandlerImplWithCorrectNullAllowed() throws IOException {
		JsonHandler tester = new JsonHandlerImpl();
		JsonNode preparedNode = tester.validateAndTransformJsonToCorrectFormat(
				JsonLoader.fromResource("/schemas/correctJsonWithNullValue.json"));
		tester.createCsvFileFromJson(preparedNode, "Berlin");
		
		String actual = readFile("Berlin.csv");
		String expected = readRecources("csvFiles/correctCsvWithNullValue.csv");
		
		Assert.assertEquals(expected, actual);
		Assert.assertTrue("The file is not deleted", removeFile("Berlin.csv"));;
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

	private String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, ENCODING);
	}

	private boolean removeFile(String fileName){
		File file = new File(fileName);	
		Assert.assertTrue("The file "+fileName+" does not exist", file.exists());
		return file.delete();

	}
	private String readRecources(String expectedcontentFileName)throws IOException {
		String expectedContent = "";
		try {
			expectedContent = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(expectedcontentFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedContent;
	}

}

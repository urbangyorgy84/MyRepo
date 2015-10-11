package com.go.euro.test.JsonHandler;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonHandlerImpl implements JsonHandler {
	private static final CsvSchema SCHEMA = CsvSchema.builder()
			.addColumn("_id",CsvSchema.ColumnType.NUMBER)
			.addColumn("name")
			.addColumn("type")
			.addColumn("latitude",CsvSchema.ColumnType.NUMBER)
			.addColumn("longitude",CsvSchema.ColumnType.NUMBER)
			.build();
	private static final String FILE_HEADER = "_id,name,type,latitude,longitude";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_EXTENSION = ".csv";
	/**
	 * Use this if you want to forbid the null values in the schema
	 * */
	@SuppressWarnings("unused")
	private static final String CITY_SCHEMA = "/City.schema";
	private static final String CITY_ALLOW_NULL_SCHEMA = "/CityAllowNullValue.schema";

	private final static Logger LOGGER = Logger.getLogger(JsonHandlerImpl.class);

	/**
	 * Create csv file from json node
	 * 
	 * @param node
	 *            node with the correct format
	 * @param city
	 *            name if the generated file
	 * @throws Exception
	 * */
	public void createCsvFileFromJson(JsonNode node, String city) {
		CsvMapper mapper = new CsvMapper();
		if (node == null) {
			LOGGER.error("The given json node cannot be null");
			return;
		}
		String csv = "";
		try {
			csv = mapper.writer(SCHEMA).writeValueAsString(node);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error during the csv file creation", e);
		}
		createFileWithContent(csv, city);
	}

	/**
	 * Validate the given JsonNode, and tranform it for the right format Use
	 * CITY_SCHEMA to forbid null value, Use CITY_ALLOW_NULL_SCHEMA to allow
	 * null value in json
	 * 
	 * @param node
	 *            json node to validate, and transform
	 * @return Validated and transformed ArrayNode
	 * @throws Exception
	 * */
	public ArrayNode validateAndTransformJsonToCorrectFormat(JsonNode node) {
		if (node == null) {
			LOGGER.error("The given json node cannot be null");
			return null;
		}
		JsonNode schemaNode = null;
		ProcessingReport report = null;
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		JsonSchema schemaValidator = null;

		try {
			schemaNode = JsonLoader.fromResource(CITY_ALLOW_NULL_SCHEMA);
			schemaValidator = factory.getJsonSchema(schemaNode);
			report = schemaValidator.validate(node);
		} catch (IOException e) {
			LOGGER.error("Error during the IO opretaion", e);
		} catch (ProcessingException e) {
			LOGGER.error("Error during the json validation", e);
		}
		if (report.isSuccess()) {
			return transformJson((ArrayNode) node);
		}
		LOGGER.error("The given json is not valid againt the schema");
		return null;
	}

	/**
	 * Create a ArrayNode with the right format,
	 * 
	 * @param node validated ArrayNode, json contains all the fields
	 * @return ArrayNode with the right format
	 * */
	protected ArrayNode transformJson(ArrayNode node) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode alignedNode = mapper.createArrayNode();
		for (int i = 0; i < node.size(); i++) {
			JsonNode nodeElement = node.get(i);
			ObjectNode alignedNodeElement = mapper.createObjectNode();
			alignedNodeElement.set("_id", nodeElement.get("_id"));
			alignedNodeElement.set("name", nodeElement.get("name"));
			alignedNodeElement.set("type", nodeElement.get("type"));
			alignedNodeElement.set("latitude", nodeElement.get("geo_position")
					.get("latitude"));
			alignedNodeElement.set("longitude", nodeElement.get("geo_position")
					.get("longitude"));
			alignedNode.add(alignedNodeElement);
		}
		return alignedNode;
	}

	/**
	 * Create a File with the given content,
	 * 
	 * @param content
	 *            content of the file
	 * @param fileName
	 *            name of the file
	 * @throws Exception
	 * */
	protected void createFileWithContent(String content, String city) {
		FileWriter fileWriter = null;
		String fileName = city + FILE_EXTENSION;

		try {
			fileWriter = new FileWriter(fileName);

			fileWriter.append(FILE_HEADER);
			fileWriter.append(NEW_LINE_SEPARATOR);

			fileWriter.append(content);
		} catch (Exception e) {
			LOGGER.error("Error when try to create a csv file", e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.error("Error when try to close the fileWriter", e);
			}

		}
	}

}

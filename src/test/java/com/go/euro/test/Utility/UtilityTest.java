package com.go.euro.test.Utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

public class UtilityTest {
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	public static String readRecources(String expectedcontentFileName)
			throws IOException {
		String expectedContent = "";
		try {
			expectedContent = IOUtils.toString(UtilityTest.class.getClassLoader()
					.getResourceAsStream(expectedcontentFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedContent;
	}
	
	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, ENCODING);
	}

	public static boolean removeFile(String fileName){
		File file = new File(fileName);	
		Assert.assertTrue("The file "+fileName+" does not exist", file.exists());
		return file.delete();

	}


}

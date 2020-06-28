package com.dnb.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.dnb.model.LogError;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorTest {

	Processor processor;

	@Before
	public void setup() {
		processor = new Processor();
	}

	@Test
	public void testValidLogFile() throws ParseException, IOException {
		Assert.assertEquals(
				"{\"errorCount\":4,\"errors\":[{\"timeStamp\":\"2020-04-01 10:10:09\",\"className\":\"ServiceClient\",\"message\":\"Error while processing row - service returned status code 400\",\"previousLogs\":[{\"timeStamp\":\"2020-04-01 10:10:09\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:10:09\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:10:08\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:10:07\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:10:06\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:10:04\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"}]},{\"timeStamp\":\"2020-04-01 10:29:59\",\"className\":\"ServiceClient\",\"message\":\"Error while processing row - service returned status code 500\",\"previousLogs\":[]},{\"timeStamp\":\"2020-04-01 10:30:02\",\"className\":\"ServiceClient\",\"message\":\"database error\",\"previousLogs\":[{\"timeStamp\":\"2020-04-01 10:29:59\",\"className\":\"ServiceClient\",\"message\":\"Error while processing row - service returned status code 500\"}]},{\"timeStamp\":\"2020-04-01 10:33:11\",\"className\":\"ServiceClient\",\"message\":\"Unhandled eror in ServiceClient: NullPointerException\",\"previousLogs\":[{\"timeStamp\":\"2020-04-01 10:33:10\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"},{\"timeStamp\":\"2020-04-01 10:33:08\",\"className\":\"ServiceClient\",\"message\":\"Service processed row\"}]}]}",
				processor.process(getFile("application.log")));
	}

	@Test
	public void testErrorCountValidLogFile() throws ParseException, IOException {
		LogError error = new ObjectMapper().readValue(processor.process(getFile("application.log")), LogError.class);
		Assert.assertEquals(error.getErrorCount(), 4);
		Assert.assertEquals(error.getErrors().size(), 4);
		Assert.assertEquals(error.getErrors().get(0).getPreviousLogs().size(), 6);
		Assert.assertEquals(error.getErrors().get(1).getPreviousLogs().size(), 0);
		Assert.assertEquals(error.getErrors().get(2).getPreviousLogs().size(), 1);
		Assert.assertEquals(error.getErrors().get(3).getPreviousLogs().size(), 2);
	}

	@Test
	public void testEmptyLogFile() throws ParseException, IOException {
		Assert.assertEquals("{\"errorCount\":0,\"errors\":[]}", processor.process(getFile("test-empty-file.log")));
	}

	@Test
	public void testErorLogFile() throws ParseException, IOException {

		String str = processor.process(getFile("test-eror.log"));
		LogError error = new ObjectMapper().readValue(str, LogError.class);
		Assert.assertEquals(error.getErrorCount(), 1);
		Assert.assertEquals(error.getErrors().size(), 1);
	}

	@Test(expected = ParseException.class)
	public void testErrorDate() throws ParseException, IOException {
		processor.process(getFile("test-wrong-date.log"));
	}

	@Test
	public void testNoPreviousLog() throws ParseException, IOException {
		String str = processor.process(getFile("test-no-previous.log"));
		LogError error = new ObjectMapper().readValue(str, LogError.class);
		Assert.assertEquals(error.getErrorCount(), 1);
		Assert.assertEquals(error.getErrors().size(), 1);
		Assert.assertEquals(error.getErrors().get(0).getPreviousLogs().size(), 0);
	}

	private BufferedReader getFile(String fileName) throws FileNotFoundException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		return new BufferedReader(new FileReader(classLoader.getResource(fileName).getFile()));
	}

}
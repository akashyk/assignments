package com.dnb.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.dnb.model.LogError;
import com.dnb.model.Errors;
import com.dnb.model.PreviousLog;
import com.dnb.model.TimedError;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Processor {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Processor.class);

	public String process(BufferedReader in) throws ParseException, IOException {

		if (in == null) {
			throw new FileNotFoundException("Log file not provided");
		}

		LogError errors = new LogError();

		Deque<TimedError> deque = new LinkedList<TimedError>();

		List<Errors> logErrors = new ArrayList<Errors>();
		int errorCount = 0;

		String line;
		long start = System.currentTimeMillis();

		while ((line = in.readLine()) != null) {
			String[] spaceSplit = line.split(" ", 4);
			String timestamp = spaceSplit[0] + " " + spaceSplit[1];

			String className = spaceSplit[2];
			String message = spaceSplit[3].replaceFirst("- ", "");

			long timeInSeconds = getTimeInSeconds(timestamp);

			TimedError timedError = new TimedError(timeInSeconds, className, message, timestamp);

			if (!hasError(message)) {
				updateLogQueue(deque, timeInSeconds, timedError);
			} else {
				List<PreviousLog> previousLogs = getPreviousLogs(deque, timeInSeconds);
				errorCount++;
				logErrors.add(new Errors(timestamp, className, message, previousLogs));
				updateLogQueue(deque, timeInSeconds, timedError);
			}
		}

		long end = System.currentTimeMillis();

		logger.info("Time taken to process the file {} seconds", (end - start) / 1000F);

		errors.setErrorCount(errorCount);
		errors.setErrors(logErrors);
		ObjectMapper obj = new ObjectMapper();
		String jsonStr = obj.writeValueAsString(errors);

		return jsonStr;

	}

	private boolean hasError(String message) {
		return StringUtils.containsIgnoreCase(message, "error") || StringUtils.containsIgnoreCase(message, "eror");
	}

	private List<PreviousLog> getPreviousLogs(Deque<TimedError> deque, long timeInSeconds) {

		List<PreviousLog> previousLogs = new ArrayList<PreviousLog>();

		if (deque.isEmpty() || timeInSeconds > deque.getLast().getTimestampSeconds() + 5) {
			return previousLogs;
		}

		Iterator<TimedError> dequeIter = deque.descendingIterator();
		while (dequeIter.hasNext()) {
			TimedError te = dequeIter.next();
			PreviousLog previousError = new PreviousLog(te.getTimestamp(), te.getClassName(), te.getMessage());
			previousLogs.add(previousError);
		}
		return previousLogs;
	}

	private void updateLogQueue(Deque<TimedError> deque, long timeInSeconds, TimedError timedError) {
		if (deque.isEmpty()) {
			deque.addLast(timedError);
		} else if ((timeInSeconds - deque.getFirst().getTimestampSeconds()) > 5) {
			while (!deque.isEmpty() && (timeInSeconds - deque.getFirst().getTimestampSeconds()) > 5) {
				deque.removeFirst();
			}

			deque.addLast(timedError);

		} else {
			deque.addLast(timedError);
		}
	}

	public long getTimeInSeconds(String timestamp) throws ParseException {
		Date date = new SimpleDateFormat("yyy-mm-dd hh:mm:ss").parse(timestamp);
		return TimeUnit.SECONDS.convert(date.getTime(), TimeUnit.MILLISECONDS);
	}
}
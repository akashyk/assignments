package com.dnb.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import com.dnb.process.Processor;

public class LogProcessor {

	public static void main(String[] args) throws ParseException, IOException {
		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		
		BufferedReader in = new BufferedReader(new FileReader(classLoader.getResource("application.log").getFile()));
		
		System.out.print(new Processor().process(in));
	}
}

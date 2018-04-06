package com.visa.innovation.paymentservice.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomLogger {

	public static synchronized void log(String data){
		System.out.println(data);
	}
	
	public static synchronized <T>void log(T data){
		System.out.println(data);
	}
	
	public static synchronized void logException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log(sw.toString());
	}
}

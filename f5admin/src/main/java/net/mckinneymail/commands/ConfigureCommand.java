package net.mckinneymail.commands;

import java.io.Console;

public class ConfigureCommand implements Runnable {

	
	
	@Override
	public void run() {
		
		
		
	}
	
	private String prompt( String prompt, String defaultValue) {
		System.out.println( prompt + " [" + defaultValue + "] : ");
		return System.console().readLine();
	}
}

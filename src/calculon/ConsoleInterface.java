package calculon;

import java.util.Scanner;

public class ConsoleInterface implements AutoCloseable {

	
	private static ConsoleInterface singleton = null;
	private Scanner scanner;
	
	
	private ConsoleInterface() {
		scanner = new Scanner(System.in);
	}
	
	public static ConsoleInterface getConsole() {
		if(singleton == null) {
			singleton = new ConsoleInterface();
		}
		
		return singleton;
	}
	
	public String getInput(String prompt) {
		System.out.println(prompt);
		String s = scanner.next();
		return s;
	}

	public void showOutput(String s) {
		System.out.println(s);
	}

	@Override
	public void close() throws Exception {
		scanner.close();
	}
}
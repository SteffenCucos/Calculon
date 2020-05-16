package calculon;

import java.util.Scanner;

public class ConsoleInterface implements AutoCloseable {

	Scanner scanner = new Scanner(System.in);
	
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
	
	static void cleanup(ConsoleInterface console) {
		try {
			console.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
package calculon;

import Expressions.Expression;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		try(ConsoleInterface console = ConsoleInterface.getConsole()) {
			
			console.showOutput("#############################");
			console.showOutput("#  _                        #");
			console.showOutput("# /   _. |  _     |  _  ._  #");
			console.showOutput("# \\_ (_| | (_ |_| | (_) | | #");
			console.showOutput("#                           #");
			console.showOutput("#############################");
			
			Parser parser = new Parser();

			while(true) {
				String eq = console.getInput("Enter an equation: ");
				if(eq.toLowerCase().equals("exit")) {
					return;
				}
				Expression expr = parser.parse(eq);
				console.showOutput(expr.toString());
				console.showOutput(expr.resolve().toString());
			}
		}
	}
}
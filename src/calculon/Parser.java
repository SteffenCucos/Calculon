package calculon;

import java.util.HashMap;
import java.util.Map;

import calculon.BinaryOperation.BinaryOperator;

public class Parser {
		
	enum State {
		BUILDING_NUMBER(null),
		
		PLUS(BinaryOperation.BinaryOperator.PLUS),
		MINUS(BinaryOperation.BinaryOperator.MINUS),
		MULTIPLY(BinaryOperation.BinaryOperator.MULTIPLY),
		DIVIDE(BinaryOperation.BinaryOperator.DIVIDE),
		EXPONENTIATE(BinaryOperation.BinaryOperator.EXPONENTIATE),
		
		L_OPERATOR(null),
		S_OPERATOR(null),
		C_OPERATOR(null),
		T_OPERATOR(null),
		
		CLOSE_EXPRESSION(null),
		OPEN_EXPRESSION(null),

		ERROR(null),
		INITIAL(null);
		
		Operator operatorMapping;
		
		State(Operator operatorMapping) {
			this.operatorMapping = operatorMapping;
		}
	}
	
	@SuppressWarnings("serial")
	public Map<State, Map<Character, State>> stateTransitions = new HashMap<State, Map<Character, State>>() {{
		put(State.BUILDING_NUMBER, new HashMap<Character, State>() {{
			put('.', State.BUILDING_NUMBER);
			put('+', State.PLUS);
			put('-', State.MINUS);
			put('*', State.MULTIPLY);
			put('/', State.DIVIDE);
			put('^', State.EXPONENTIATE);
			put(')', State.CLOSE_EXPRESSION);
			putAll(numberMatchingMap());
		}});
		put(State.PLUS, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.MINUS, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.MULTIPLY, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.DIVIDE, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.EXPONENTIATE, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.INITIAL, new HashMap<Character, State>() {{
			put('-', State.BUILDING_NUMBER);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.OPEN_EXPRESSION, new HashMap<Character, State>() {{
			put('-', State.BUILDING_NUMBER);
			put('(', State.OPEN_EXPRESSION);
			//put(')', State.CLOSE_EXPRESSION);
			putAll(numberMatchingMap());
			putAll(unaryMatchingMap());
		}});
		put(State.CLOSE_EXPRESSION, new HashMap<Character, State>(){{
			put('.', State.BUILDING_NUMBER);
			put('+', State.PLUS);
			put('-', State.MINUS);
			put('*', State.MULTIPLY);
			put('/', State.DIVIDE);
			put('^', State.EXPONENTIATE);
			put(')', State.CLOSE_EXPRESSION);
		}});
	}};
	
	@SuppressWarnings("serial")
	public Map<Character, State> unaryMatchingMap() {
		return new HashMap<Character, State>() {{
			put('s', State.S_OPERATOR);
			put('c', State.C_OPERATOR);
			put('t', State.T_OPERATOR);
			put('l', State.L_OPERATOR);
		}};
	}
	
	@SuppressWarnings("serial")
	public Map<Character, State> numberMatchingMap() {
		return new HashMap<Character, State>() {{
			put('0', State.BUILDING_NUMBER);
			put('1', State.BUILDING_NUMBER);
			put('2', State.BUILDING_NUMBER);
			put('3', State.BUILDING_NUMBER);
			put('4', State.BUILDING_NUMBER);
			put('5', State.BUILDING_NUMBER);
			put('6', State.BUILDING_NUMBER);
			put('7', State.BUILDING_NUMBER);
			put('8', State.BUILDING_NUMBER);
			put('9', State.BUILDING_NUMBER);
		}};
	}
	
	public Expression parse(String expression) {
		expression = expression
				// remove spaces & tabs
				.replaceAll(" ", "")
				.replaceAll("	", "")
				.toLowerCase();
		return parseExpression(new CharacterIterator(expression), State.INITIAL);
	}
	
	public boolean nextContains(CharacterIterator characters, String search) {
		
		for(char c : search.toCharArray()) {
			Character next = characters.next();
			if(!next.equals(c)) {
				return false;
			}
		}
		
		
		return true;
	}
	
	public Expression parseUnaryOperation(CharacterIterator characters, State XExpr) {
		
		UnaryOperation.UnaryOperator operator = null;
		
		switch(XExpr) {
			case S_OPERATOR:
				if(nextContains(characters, "in(")) {
					operator = UnaryOperation.UnaryOperator.SIN;
				}
				break;
			case C_OPERATOR:
				if(nextContains(characters, "os(")) {
					operator = UnaryOperation.UnaryOperator.COS;
				}
				break;
			case T_OPERATOR:
				if(nextContains(characters, "an(")) {
					operator = UnaryOperation.UnaryOperator.TAN;
				}
				break;
			case L_OPERATOR:
				Character next = characters.peek();
				if(next == 'n' && nextContains(characters, "n(")) {
					operator = UnaryOperation.UnaryOperator.LN;
				} else if(next == 'o' && nextContains(characters, "og(")) {
					operator = UnaryOperation.UnaryOperator.LOG;
				}
				break;
			case OPEN_EXPRESSION:
				operator = UnaryOperation.UnaryOperator.BRACKET;
				break;
		}
		
		if(operator == null) {
			throw new RuntimeException("");
		}
		
		String contained = "";
		int openCount = 1;
		int closedCount = 0;
		
		while(characters.hasNext()) {
			Character character = characters.next();
			if(character.equals('(')) {
				openCount++;
			} else if(character.equals(')')) {
				closedCount++;
			}
			
			if(openCount == closedCount) {
				break;
			}
			
			contained += character;
		}
		
		Expression dependant = parseExpression(new CharacterIterator(contained), State.INITIAL);
		return new UnaryOperation(dependant, operator);
	}
	
	public Expression parseNumber(CharacterIterator characters, Character firstChar) {
		
		String value = firstChar.toString();
		
		State curState = State.BUILDING_NUMBER;
		while(characters.hasNext()) {
			Character character = characters.peek();
			
			State nextState = getNextState(curState, character); 
			
			if(nextState == State.ERROR) {
				return new ErrorExpression("Invalid state transition from state '" + curState.toString() + "' with character '" + character + "'");
			} else if(nextState == State.BUILDING_NUMBER) {
				value += characters.next();
			} else {
				break;
			}
		}
		
		return new ValueExpression(value);
	}
	
	public Expression parseExpression(CharacterIterator characters, State initialState) {
		

		State curState = initialState;
		Expression rootExpression = null;
		
		while(characters.hasNext()) {
			Character character = characters.next();
			
			State nextState = getNextState(curState, character); 

			Expression nextExpr;
			
			switch(nextState) {
				case BUILDING_NUMBER:
					rootExpression = parseNumber(characters, character);
					
					if(rootExpression instanceof ErrorExpression) {
						return rootExpression;
					}
					
					break;
				case PLUS:
				case MINUS:
				case MULTIPLY:
				case DIVIDE:
				case EXPONENTIATE:
					nextExpr = parseExpression(characters, nextState);
					
					if(nextExpr instanceof ErrorExpression) {
						return nextExpr;
					}
					
					rootExpression = BinaryOperation.attatchNewExpression(nextExpr, rootExpression, (BinaryOperator)nextState.operatorMapping);
					break;
				case S_OPERATOR:
				case C_OPERATOR:
				case T_OPERATOR:
				case L_OPERATOR:
				case OPEN_EXPRESSION:
					nextExpr = parseUnaryOperation(characters, nextState);
					if(nextExpr instanceof ErrorExpression) {
						return nextExpr;
					}
					
					rootExpression = nextExpr;
					nextState = State.BUILDING_NUMBER;
					break;
				case CLOSE_EXPRESSION:
					return rootExpression;
				case ERROR:
				default:
					return new ErrorExpression("Invalid state transition from state '" + curState.toString() + "' with character '" + character + "'");
			}
			
			curState = nextState;
		}
		
		return rootExpression;
	}
	
	public State getNextState(State currentState, Character character) {
		
		return stateTransitions.get(currentState).getOrDefault(character, State.ERROR);
	}	
}
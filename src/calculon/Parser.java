package calculon;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

import calculon.BinaryOperation.BinaryOperator;

public class Parser {
		
	enum State {
		BUILDING_NUMBER(null),
		
		PLUS(BinaryOperation.BinaryOperator.PLUS),
		MINUS(BinaryOperation.BinaryOperator.MINUS),
		MULTIPLY(BinaryOperation.BinaryOperator.MULTIPLY),
		DIVIDE(BinaryOperation.BinaryOperator.DIVIDE),
		EXPONENTIATE(BinaryOperation.BinaryOperator.EXPONENTIATE),
		
//		SIN,
//		COS,
//		TAN,
//		L_WORD,
//		LOG,
//		LN,
		
		OPEN_EXPRESSION(null),
		CLOSE_EXPRESSION(null),
		
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
		}});
		put(State.MINUS, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
		}});
		put(State.MULTIPLY, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
		}});
		put(State.DIVIDE, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
		}});
		put(State.EXPONENTIATE, new HashMap<Character, State>() {{
			put('-', State.MINUS);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
		}});
		put(State.INITIAL, new HashMap<Character, State>() {{
			put('-', State.BUILDING_NUMBER);
			put('(', State.OPEN_EXPRESSION);
			putAll(numberMatchingMap());
		}});
	}};
	
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
				.replaceAll("	", "");
		return parseExpression(new CharacterIterator(expression), State.INITIAL);
	}
	
	public ValueExpression parseNumber(CharacterIterator characters, Character firstChar) {
		
		String value = firstChar.toString();
		
		State curState = State.BUILDING_NUMBER;
		while(characters.hasNext()) {
			Character character = characters.peek();
			
			State nextState = getNextState(curState, character); 
			
			if(nextState == State.ERROR) {
				System.out.println("Invalid state transition from state '" + curState.toString() + "' with character '" + character + "'");
				System.exit(0);
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
					break;
				case PLUS:
				case MINUS:
				case MULTIPLY:
				case DIVIDE:
				case EXPONENTIATE:
					nextExpr = parseExpression(characters, nextState);
					rootExpression = BinaryOperation.attatchNewExpression(nextExpr, rootExpression, (BinaryOperator)nextState.operatorMapping);
					break;
				case ERROR:
				default:
					System.out.println("Invalid state transition from state '" + curState.toString() + "' with character '" + character + "'");
					System.exit(0);
					break;
					
			}
			
			curState = nextState;
		}
		
		return rootExpression;
	}
	
	public State getNextState(State currentState, Character character) {
		return stateTransitions.get(currentState).getOrDefault(character, State.ERROR);
	}	
}
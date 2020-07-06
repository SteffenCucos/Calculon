package Expressions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BinaryOperation extends OperationExpression {

	public enum BinaryOperator implements Operator {
		PLUS,
		MINUS,
		MULTIPLY,
		DIVIDE,
		EXPONENTIATE;
		
		public boolean hasPriority(Operator other) {
			
			if(this == PLUS || this == MINUS) {
				boolean b = other == PLUS || other == MINUS;
				return b;
			}
			
			return true;
		}
	}

	Expression left, right;
	
	public BinaryOperation(Expression left, Expression right, BinaryOperator operator) {
		super(operator);
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Returns the new root after attaching the new expression to the tree
	 */
	public static OperationExpression attatchNewExpression(Expression newExpression, Expression rootExpression, BinaryOperator operator) {
		if(newExpression instanceof BinaryOperation) {
			BinaryOperation operation = (BinaryOperation)newExpression;
			if(operator.hasPriority(operation.operator)) {
				BinaryOperation.swap(rootExpression, operation, operator);
				return operation;
			}
		} 
		
		return new BinaryOperation(rootExpression, newExpression, operator);
	}
	
	public static void swap(Expression toSwap, BinaryOperation right, BinaryOperator operator) {
		Expression left = right.left;
		
		if(left instanceof BinaryOperation && operator.hasPriority((((BinaryOperation) left).operator))) {
			swap(toSwap, (BinaryOperation)left, operator);
			return;
		}
		
		if(operator.hasPriority(right.operator)) {
			Expression newLeft = new BinaryOperation(toSwap, left, operator);
			right.left = newLeft;
		} else {
			BinaryOperator rOperator = (BinaryOperator)right.operator;
			Expression rLeft = right.left;
			Expression rRight = right.right;
			
			right.operator = operator;
			right.left = toSwap;
			right.right = new BinaryOperation(rLeft, rRight, rOperator);
		}
	}
	
	@Override
	public BigDecimal resolve() {
		BigDecimal leftValue = left.resolve();
		BigDecimal rightValue = right.resolve();
		
		switch((BinaryOperator)operator) {
			case PLUS:
				return leftValue.add(rightValue);
			case MINUS:
				return leftValue.subtract(rightValue);
			case MULTIPLY:
				return leftValue.multiply(rightValue);
			case DIVIDE:
				return leftValue.divide(rightValue, 5, RoundingMode.HALF_UP);
			case EXPONENTIATE:
				BigDecimal accumulation = BigDecimal.ONE;
				for(int i = 0; i < rightValue.intValue(); i++) {
					accumulation = accumulation.multiply(leftValue);
				}
				return accumulation;
			default:
				return null;
		}
	}
	
	@Override
	public String toString() {
		return "(" + left.toString() + " " + operator.toString() + " " + right.toString() +  ")";
	}
	
}

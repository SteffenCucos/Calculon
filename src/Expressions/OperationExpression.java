package Expressions;

public abstract class OperationExpression implements Expression {

	Operator operator;
	
	public OperationExpression(Operator operator) {
		this.operator = operator;
	}
}

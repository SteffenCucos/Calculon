package Expressions;

import java.math.BigDecimal;

public class UnaryOperation extends OperationExpression {

	public enum UnaryOperator implements Operator {
		BRACKET,
		SIN,
		COS,
		TAN,
		LOG,
		LN,;

		@Override
		public boolean hasPriority(Operator other) {
			return false;
		}
	}
	
	Expression dependant;

	public UnaryOperation(Expression dependant, UnaryOperator operator) {
		super(operator);
		this.dependant = dependant;
	}
	
	@Override
	public BigDecimal resolve() {
		
		BigDecimal dependantValue = dependant.resolve();
		double value;
		
		switch((UnaryOperator)operator) {
			case SIN:
				value = Math.sin(dependantValue.doubleValue());
				break;
			case COS:
				value = Math.cos(dependantValue.doubleValue());
				break;
			case TAN:
				value = Math.tan(dependantValue.doubleValue());
				break;
			case LOG:
				value = Math.log10(dependantValue.doubleValue());
				break;
			case LN:
				value = Math.log(dependantValue.doubleValue());
				break;
			default:
				value = dependantValue.doubleValue();
		}
		return new BigDecimal(value);
	}
	
	@Override
	public String toString() {
		switch((UnaryOperator)operator) {
			case BRACKET:
				return "(" + dependant.toString() + ")";
			default:
				return operator.toString() + "(" + dependant.toString() + ")";
		}
	}
}
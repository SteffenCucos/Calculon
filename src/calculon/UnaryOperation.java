package calculon;

import java.math.BigDecimal;

public class UnaryOperation extends OperationExpression {

	enum UnaryOperator {
		SIN,
		COS,
		TAN,
		LOG,
		LN,
	}
	
	Expression dependant;

	public UnaryOperation(Expression dependant, Operator operator) {
		super(operator);
		this.dependant = dependant;
	}
	
	@Override
	public BigDecimal resolve() {
		return null;
	}
	
	
	
}

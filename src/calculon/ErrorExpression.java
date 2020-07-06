package calculon;

import java.math.BigDecimal;

public class ErrorExpression implements Expression {

	String error;
	
	public ErrorExpression(String error) {
		this.error = error;
	}
	
	@Override
	public BigDecimal resolve() {
		return BigDecimal.ZERO;
	}
	
	@Override
	public String toString() {
		return error;
	}
}

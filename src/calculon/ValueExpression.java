package calculon;

import java.math.BigDecimal;

public class ValueExpression implements Expression {

	BigDecimal value;
	
	public ValueExpression(String value) {
		this.value = new BigDecimal(value);
	}
	
	public ValueExpression(BigDecimal value) {
		this.value = value;
	}
	
	@Override
	public BigDecimal resolve() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
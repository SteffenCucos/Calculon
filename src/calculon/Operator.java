package calculon;

public interface Operator {
	boolean hasPriority(Operator other);
}

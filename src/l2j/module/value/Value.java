package l2j.module.value;

public abstract class Value {
	public ValueType type;
	public Value(ValueType v) {
		this.type = v;
	}
	
	// Do nothing...
	public void markPointerized() {
		
	}
	
	public abstract String toString();
}

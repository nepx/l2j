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
	
	public abstract char getJavaSignatureType();
	
	public abstract void write(byte[] out, int pos);
	
	public int getAddress() {
		throw new IllegalStateException("cannot get addr of " + type);
	}
}

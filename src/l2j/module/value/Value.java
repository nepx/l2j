package l2j.module.value;

import l2j.module.types.Type;

public abstract class Value {
	public ValueType type;
	public Type llvmType;
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
	
	public static final Value CONST_1 = new ValueConstant(1);
}

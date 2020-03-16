package l2j.module.value;

public class ValueConstant extends Value {
	public int value;
	public ValueConstant(int value) {
		super(ValueType.Constant);
		this.value = value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

	public char getJavaSignatureType() {
		return 'I';
	}
}

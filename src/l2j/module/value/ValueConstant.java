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

	public void write(byte[] out, int pos) {
		// TODO: 64-bit constants?
		out[pos] = (byte) value;
		out[pos + 1] = (byte) (value >> 8);
		out[pos + 2] = (byte) (value >> 16);
		out[pos + 3] = (byte) (value >> 24);
	}
}

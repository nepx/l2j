package l2j.module.value;

public class ValueGlobalVariable extends Value {
	
	public String name;
	public ValueGlobalVariable(String s) {
		super(ValueType.GlobalVariable);
		name = s;
	}

	@Override
	public String toString() {
		return "@" + name;
	}

	public char getJavaSignatureType() {
		return 'I';
	}
}

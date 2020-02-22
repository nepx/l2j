package l2j.module.value;

public class ValueGlobalVariable extends Value {
	
	String name;
	public ValueGlobalVariable(String s) {
		super(ValueType.GlobalVariable);
		name = s;
	}

	@Override
	public String toString() {
		return "@" + name;
	}

}

package l2j.module.value;

public class ValueLocalVariable extends Value {
	public String name;
	public ValueLocalVariable(String name) {
		super(ValueType.LocalVariable);
		this.name = name;
	}
}

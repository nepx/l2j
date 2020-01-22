package l2j.module.value;

import l2j.module.function.Function;
import l2j.module.function.LocalVariable;

public class ValueLocalVariable extends Value {
	public String name;
	private LocalVariable backingVar;

	public ValueLocalVariable(String name, Function f) {
		super(ValueType.LocalVariable);
		this.name = name;
		this.backingVar = f.lvars.get(name);
		if (this.backingVar == null)
			throw new IllegalStateException("Referencing a variable (" + name + ") that doesn't exist!!");
	}
}

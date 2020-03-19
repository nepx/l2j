package l2j.module.value;

import l2j.module.types.Type;

public class GlobalValueVector {
	public Type type;
	public Value value;
	public boolean inrange;

	public GlobalValueVector(Type t, Value v) {
		type = t;
		value = v;
		inrange = false;
	}
	public GlobalValueVector(Type t, Value v, boolean inrange) {
		type = t;
		value = v;
		this.inrange = inrange;
	}
	
	public String toString() {
		return String.format("%s %s", type.toString(), value.toString());
	}
}

package l2j.module.value;

import java.util.ArrayList;

import l2j.module.types.Type;

public class ValueGetElementPtr extends Value {

	public boolean inbounds;
	public Type type;
	public Value value;
	public ArrayList<TypeValuePair> tvps;

	public ValueGetElementPtr(boolean inbounds, Type type, Value v, ArrayList<TypeValuePair> al) {
		super(ValueType.GetElementPtr);
		this.inbounds = inbounds;
		this.type = type;
		this.value = v;
		this.tvps = al;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("getelementptr ");
		if (inbounds)
			b.append("inbounds ");
		b.append("(");
		b.append(type.toString());
		b.append(", ");
		return b.toString();
	}

	public static class TypeValuePair {
		public Type type;
		public Value value;
	}
}

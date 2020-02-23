package l2j.module.value;

import l2j.module.types.Type;

public class ValueGetElementPtr extends Value {
	
	public boolean inbounds;
	public Type type;
	public ValueGetElementPtr(boolean inbounds, Type type) {
		super(ValueType.GetElementPtr);
		this.inbounds = inbounds;
		this.type = type;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("getelementptr ");
		if(inbounds) b.append("inbounds ");
		b.append("(");
		b.append(type.toString());
		b.append(", ");
		return b.toString();
	}

}

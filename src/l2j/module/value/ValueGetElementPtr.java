package l2j.module.value;

import java.util.ArrayList;

import l2j.module.types.Type;

public class ValueGetElementPtr extends Value {

	public boolean inbounds;
	public Type type;
	public ArrayList<GlobalValueVector> list;

	public ValueGetElementPtr(boolean inbounds, Type type, ArrayList<GlobalValueVector> list) {
		super(ValueType.GetElementPtr);
		this.inbounds = inbounds;
		this.type = type;
		this.list = list;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("getelementptr ");
		if (inbounds)
			b.append("inbounds ");
		b.append("(");
		b.append(type.toString());
		b.append(", ");
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i != 0)
				b.append(", ");
			b.append(list.get(i));
		}
		b.append(")");
		return b.toString();
	}

	public char getJavaSignatureType() {
		return 'I';
	}

	public void write(byte[] out, int pos) {
		throw new IllegalStateException("getelementptr does not go in the data section!");
	}
}

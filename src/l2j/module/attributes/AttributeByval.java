package l2j.module.attributes;

import l2j.module.types.Type;

public class AttributeByval extends Attribute {

	public Type byval_type;

	public AttributeByval(Type t) {
		super(AttributeType.Byval);
		byval_type = t;
	}

	public String toString() {
		if (byval_type == null)
			return "byval";
		else
			return "byval(" + byval_type.toString() + ")";
	}
}

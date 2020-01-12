package l2j.module.attributes;

public abstract class Attribute {
	public AttributeType type;
	public Attribute(AttributeType t) {
		type = t;
	}
	public abstract String toString();
}

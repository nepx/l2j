package l2j.module.attributes;

public class AttributeAlign extends Attribute {
	public int alignment;
	public AttributeAlign(int alignment) {
		super(AttributeType.Align);
		this.alignment = alignment;
	}
	
	public String toString() {
		return "align " + alignment;
	}
}

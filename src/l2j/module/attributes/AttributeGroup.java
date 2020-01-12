package l2j.module.attributes;

public class AttributeGroup extends Attribute {
	public int id;
	public AttributeGroup(int id) {
		super(AttributeType.Group);
		this.id = id;
	}
	
	public String toString() {
		return "#" + id;
	}
}

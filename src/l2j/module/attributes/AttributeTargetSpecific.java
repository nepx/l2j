package l2j.module.attributes;

public class AttributeTargetSpecific extends Attribute {
	public String key, value;
	public AttributeTargetSpecific(String key, String value) {
		super(AttributeType.TargetSpecific);
		this.key=key;
		this.value=value;
	}
	
	public String toString() {
		if(value == null) return key;
		StringBuilder s = new StringBuilder();
		s.append('"');
		s.append(key);
		s.append("\"=\"");
		s.append(value);
		s.append('"');
		return s.toString();
	}
}

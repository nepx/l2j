package l2j.module.attributes;

/**
 * Generic attribute
 * @author jkim13
 *
 */
public class AttributeGeneric extends Attribute{

	public AttributeGeneric(AttributeType t) {
		super(t);
	}

	public String toString() {
		return type.toString().toLowerCase();
	}

}

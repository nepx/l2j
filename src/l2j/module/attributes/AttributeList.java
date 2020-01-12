package l2j.module.attributes;

import java.util.ArrayList;

/**
 * List of attributes. Thin wrapper over ArrayList
 * 
 * @author jkim13
 *
 */
public class AttributeList {
	public ArrayList<Attribute> arr = new ArrayList<Attribute>();

	public void add(Attribute a) {
		arr.add(a);
	}

	public Attribute get(int i) {
		return arr.get(i);
	}
}

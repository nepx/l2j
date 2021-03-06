package l2j.module.function;

import java.util.ArrayList;

import l2j.module.attributes.Attribute;
import l2j.module.types.Type;

public class Parameter {
	public Type type;
	public ArrayList<Attribute> attrs = new ArrayList<Attribute>();
	public String name;
	public int flags = 0;

	public static final int NOCAPTURE = (1 << 0);
	public static final int READONLY = (1 << 1);
	public static final int READNONE = (1 << 2);
}

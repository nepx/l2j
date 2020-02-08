package l2j.module;

public class Linkage {
	static public final int VISIBILITY_PRIVATE = 1; // Not visible at all. Other applications should not know it exists
	// at all
	static public final int VISIBILITY_INTERNAL = 2; // Not linkable, but makes a mention in the object file.
	static public final int VISIBILITY_EXTERNAL = 3; // External symbol
	static public final int VISIBILITY_DEFAULT = 0; // Default visibility

	public int value;
}

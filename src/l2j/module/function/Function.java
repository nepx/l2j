package l2j.module.function;

import java.util.ArrayList;
import java.util.HashMap;

import l2j.module.attributes.*;
import l2j.module.types.Type;

public class Function {
	// Function visibilities - note that these are ignored; you can redefine any
	// kind of symbol
	static public final int VISIBILITY_PRIVATE = 1; // Not visible at all. Other applications should not know it exists
													// at all
	static public final int VISIBILITY_INTERNAL = 2; // Not linkable, but makes a mention in the object file.
	static public final int VISIBILITY_EXTERNAL = 3; // External symbol
	static public final int VISIBILITY_DEFAULT = 0; // Default visibility

	public int visibillity;

	public ArrayList<Attribute> returnAttributes = new ArrayList<Attribute>();

	public Type returnType;

	public String name;
	public ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
	// The function attributes themselves
	public ArrayList<Attribute> attributess = new ArrayList<Attribute>();
	// LLVM can have named basic blocks, so we must index by name
	public HashMap<String, BasicBlock> blocks = new HashMap<String, BasicBlock>();
	public HashMap<String, LocalVariable> lvars = new HashMap<String, LocalVariable>();

	private int internalNumberingID;

	public String getTempName() {
		return Integer.toString(internalNumberingID++);
	}
}

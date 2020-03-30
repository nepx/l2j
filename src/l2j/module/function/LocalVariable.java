package l2j.module.function;

import l2j.module.types.Type;

public class LocalVariable extends Variable {
	// Local variable
	public int lvarid, usageCount;
	public String name;
	public Type type;
	
	public LocalVariable(String name, Function f) {
		super(false);
		this.name = name;
		this.lvarid = f.getLocalVariableID();
	}
	
	public int getID() {
		return lvarid;
	}
}

package l2j.module.function;

public class LocalVariable extends Variable {
	// Local variable
	public int lvarid;
	public String name;
	
	public LocalVariable(String name, Function f) {
		super(false);
		this.name = name;
		this.lvarid = f.getLocalVariableID();
	}
	
	public int getID() {
		return lvarid;
	}
}

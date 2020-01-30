package l2j.module.function;

public abstract class Variable {
	public boolean global;
	public int alignment;
	
	public Variable(boolean isGlobal) {
		global = isGlobal;
	}

	public abstract int getID();
}

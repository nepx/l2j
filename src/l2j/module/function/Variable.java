package l2j.module.function;

public abstract class Variable {
	public boolean global;
	public Variable(boolean isGlobal) {
		global = isGlobal;
	}

	public abstract int getID();
}

package l2j.module.function;

public class LocalVariable {
	// Is the address of this value used? (Otherwise, we'll have to keep it in a space in memory)
	public boolean pointerized;
	public String name;
	
	public LocalVariable(String name) {
		this.name = name;
	}
}

package l2j.translator;

import java.io.IOException;

public class ClassFileEmitter {
	private String dest;
	private StringBuilder lines = new StringBuilder();

	public ClassFileEmitter(String dest) {
		this.dest = dest;
		lines.append(".super java/lang/Object\n");
	}

	/**
	 * Set generated class file name
	 * 
	 * @param name
	 */
	public void setClassName(String name) {
		lines.append(".class public ");
		lines.append(name);
		lines.append("\n");

		// stupid check
		if (dest.indexOf(name) == -1)
			throw new IllegalStateException("Bad class name");
	}
	
	/**
	 * Create new method
	 * @param access
	 * @param spec
	 */
	public void createMethod(String access, String spec) {
		lines.append(".method ");
		lines.append(access);
		lines.append(" ");
		lines.append(spec);
		lines.append("\n");
	}
	
	/**
	 * End method declaration
	 */
	public void endMethod() {
		lines.append(".end method\n");
	}
	
	/**
	 * Set number of locals
	 * @param locals
	 */
	public void setLocals(int locals) {
		lines.append(".limit locals ");
		lines.append(locals);
		lines.append("\n");
	}
}

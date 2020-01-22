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
	 * Set number of locals
	 * @param locals
	 */
	public void setLocals(int locals) {
		
	}
}

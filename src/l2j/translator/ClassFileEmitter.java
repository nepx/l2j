package l2j.translator;

import java.io.IOException;
import java.io.PrintWriter;

public class ClassFileEmitter {
	private String dest;
	private StringBuilder lines = new StringBuilder();

	public ClassFileEmitter(String dest) {
		this.dest = dest + ".jasmin";
	}

	/**
	 * Set generated class file name
	 * 
	 * @param name
	 */
	public void setClassName(String name) {
		lines.append(".class public l2j/generated/");
		lines.append(name);
		lines.append("\n");

		// stupid check
		if (dest.indexOf(name) == -1)
			throw new IllegalStateException("Bad class name");
	}

	/**
	 * Create new method
	 * 
	 * @param access
	 * @param spec
	 */
	public void createMethod(String access, String spec) {
		lines.append(".method ");
		lines.append(access);
		lines.append(" ");
		lines.append(spec);
		lines.append("\n.limit stack 10\n");
	}

	/**
	 * End method declaration
	 */
	public void endMethod() {
		lines.append(".end method\n");
	}

	/**
	 * Set number of locals
	 * 
	 * @param locals
	 */
	public void setLocals(int locals) {
		lines.append(".limit locals ");
		lines.append(locals);
		lines.append("\n");
	}

	/**
	 * Create label
	 * 
	 * @param name
	 */
	public void label(String name) {
		lines.append(name);
		lines.append(":\n");
	}

	/**
	 * Generate an invokestatic function call. All arguments must be pushed to the
	 * stack beforehand.
	 * 
	 * @param methodSignature
	 */
	public void invokeStatic(String methodSignature) {
		lines.append("invokestatic ");
		lines.append(methodSignature);
		lines.append("\n");
	}
	
	/**
	 * Push an integer constant to stack
	 * @param x
	 */
	public void pushInt(int x) {
		if (x <= 5 && x >= -1) {
			lines.append("iconst_");
			if (x == -1)
				lines.append("m1");
			else
				lines.append(x);
			lines.append("\n");
		} else {
			lines.append("bipush ");
			lines.append(x);
			lines.append("\n");
		}
	}
	
	/**
	 * Pop an integer off the stack and store it in a local variable
	 * @param varid
	 */
	public void storeIntToVariable(int varid) {
		if(varid <= 3) {
			lines.append("istore_");
			lines.append(varid);
		}else {
			lines.append("istore ");
			lines.append(varid);
		}
		lines.append("\n");
	}
	
	public void loadIntFromVariable(int varid) {
		if(varid <= 3) {
			lines.append("iload_");
			lines.append(varid);
		}else {
			lines.append("iload ");
			lines.append(varid);
		}
		lines.append("\n");
	}

	public void dump() {
		System.out.println(lines.toString());
	}
	
	public void returnInteger() {
		lines.append("ireturn\n");
	}
	
	public void write() {
		try {
			System.out.println(dest);
			PrintWriter out = new PrintWriter(dest);
			out.print(lines.toString());
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("could not write to file");
		}
	}
	
	private String superclass;
	
	public void setExtends(String name) {
		lines.append(".super ");
		lines.append(name);
		lines.append("\n");
		superclass = name;
	}
	public void generateDefaultConstructor() {
		lines.append(".method public <init>()V\naload_0\ninvokespecial ");
		lines.append(superclass);
		lines.append("/<init>()V\nreturn\n.end method\n");
	}
}

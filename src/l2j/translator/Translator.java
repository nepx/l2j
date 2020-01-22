package l2j.translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import l2j.module.Module;
import l2j.module.function.Function;
import l2j.module.function.instruction.Instruction;
import l2j.module.function.BasicBlock;

/**
 * Things to do here...
 * 
 * - Find out which allocas can be "flattened" (turned into local variables)
 * 
 * @author jkim13
 *
 */
public class Translator {
	private Module m;
	private String basename;

	/**
	 * LLVM module to Java bytecode translator
	 * 
	 * @param m
	 * @param b
	 */
	public Translator(Module m, String b) {
		this.m = m;
		this.basename = b;
	}

	private String sanitizeName(String name) {
		return name.replaceAll("[^A-Za-z0-9_$]", "_");
	}

	/**
	 * Translate a single function to Java bytecode
	 */
	public void translateFunction(Function f) {
		// We have to break down this function into multiple Java basic blocks.
		// Luckily, the parser has already done that for us.

		String className = sanitizeName(f.name);
		ClassFileEmitter cf = new ClassFileEmitter(basename + className);

		// Build method signature
		StringBuilder sig = new StringBuilder();
		sig.append("call(");
		int paramCount = f.parameters.size();
		for (int i = 0; i < paramCount; i++) {
			sig.append(f.parameters.get(i).type.getJavaSignatureType());
		}
		sig.append(")");
		sig.append(f.returnType.getJavaSignatureType());
		cf.createMethod("public static", sig.toString());
		cf.setLocals(f.lvars.size());

		Set<Map.Entry<String, BasicBlock>> es = f.blocks.entrySet();
		for (Map.Entry<String, BasicBlock> entry : es) {
			
		}
		
		cf.endMethod();
		cf.dump();
	}
}

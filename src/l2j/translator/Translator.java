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
 *  - Find out which allocas can be "flattened" (turned into local variables)
 * @author jkim13
 *
 */
public class Translator {
	private Module m;
	private BufferedWriter b;
	
	/**
	 * LLVM module to Java bytecode translator
	 * @param m
	 * @param b
	 */
	public Translator(Module m, BufferedWriter b) {
		this.m = m;
		this.b = b;
	}
	
	// Backend emitters
	private void emitLabel(String name) throws IOException {
		b.append(name);
		b.append(":\n");
	}
	
	/**
	 * Translate a single function to Java bytecode
	 */
	public void translateFunction(Function f) throws IOException {
		// We have to break down this function into multiple Java basic blocks.
		// Luckily, the parser has already done that for us.
		
		Set<Map.Entry<String, BasicBlock>> es = f.blocks.entrySet();
		for(Map.Entry<String, BasicBlock> entry : es) {
		}
	}
}

package l2j.translator;

import java.util.Map;
import java.util.Set;

import l2j.module.Module;
import l2j.module.function.Function;
import l2j.module.function.BasicBlock;
import l2j.module.function.instruction.*;
import l2j.module.types.TypeType;
import l2j.module.value.*;

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
	 * Push a single value onto the stack
	 * 
	 * @param cf
	 * @param v
	 */
	private void loadValue(ClassFileEmitter cf, Value v) {
		switch (v.type) {
		case Constant:
			cf.pushInt(((ValueConstant) v).value);
			break;
		case LocalVariable:
			cf.loadIntFromVariable(((ValueLocalVariable) v).getID());
			break;
		default:
			throw new UnsupportedOperationException("TODO: load value: " + v.type);
		}
	}

	/**
	 * Call this before you exit a function!
	 * 
	 * @param cf
	 * @param totalBytesAllocated
	 */
	private void procExitHandler(ClassFileEmitter cf, int totalBytesAllocated) {
		cf.pushInt(totalBytesAllocated);
		cf.invokeStatic("l2j/runtime/Memory/dealloca(I)V");
	}

	/**
	 * Generate call(I)I
	 * 
	 * @param cf
	 * @param f
	 * @param sig      Method signature
	 * @param thisName Name of this class
	 */
	private void generateCall(ClassFileEmitter cf, Function f, String sig, String thisName) {
		cf.createMethod("public", "call(I)I");
		if (f.parameters.size() != 0)
			throw new IllegalStateException("Generate parameter loading idk");
		cf.invokeStatic(thisName + "/" + sig);
		if (f.returnType.type == TypeType.Void)
			cf.returnVoid();
		else
			cf.returnInteger();
		cf.endMethod();
	}

	/**
	 * Translate a single function to Java bytecode
	 */
	public void translateFunction(Function f) {
		// We have to break down this function into multiple Java basic blocks.
		// Luckily, the parser has already done that for us.

		String className = sanitizeName(f.name);
		ClassFileEmitter cf = new ClassFileEmitter(basename + className);
		cf.setClassName(className);
		cf.setExtends("l2j/runtime/FunctionImpl");
		cf.generateDefaultConstructor();

		// Build method signature
		StringBuilder sig = new StringBuilder();
		sig.append("exec(");
		int paramCount = f.parameters.size();
		for (int i = 0; i < paramCount; i++) {
			sig.append(f.parameters.get(i).type.getJavaSignatureType());
		}
		sig.append(")");
		sig.append(f.returnType.getJavaSignatureType());

		// Create our call() method
		String sigStr = sig.toString();
		generateCall(cf, f, sigStr, className);

		cf.createMethod("public static", sigStr);
		cf.setLocals(f.lvars.size());

		Set<Map.Entry<String, BasicBlock>> es = f.blocks.entrySet();
		int totalBytesAllocated = 0;
		for (Map.Entry<String, BasicBlock> entry : es) {
			cf.label("label" + sanitizeName(entry.getKey()));

			BasicBlock b = entry.getValue();
			int insnCount = b.instructions.size();

			for (int i = 0; i < insnCount; i++) {
				Instruction insn = b.instructions.get(i);
				switch (insn.type) {
				case Alloca: {
					// Bytecode breakdown:
					// 1. Push the number of bytes to allocate
					// 2. Call the allocation function
					// 3. Store the result in a local variable
					// TODO: Call alloca once to get a chunk of stack, and allocate internally from
					// there

					InstructionAlloca ia = (InstructionAlloca) insn;
					int bytesToAllocate = ia.type.getSize() * ia.numElements;
					cf.pushInt(bytesToAllocate);
					totalBytesAllocated += bytesToAllocate;
					cf.invokeStatic("l2j/runtime/Memory/alloca(I)I");
					cf.storeIntToVariable(ia.destination.getID());

					// If the alloca is aligned, then indicate that the current local variable is
					// aligned, too.
					ia.destination.alignment = ia.align;
					break;
				}
				case Store: {
					// Bytecode breakdown
					// 1. Load requested value from stack
					// 2. Load address to stack
					// 2. Call a function to write to memory
					InstructionStore is = (InstructionStore) insn;
					loadValue(cf, is.value);
					loadValue(cf, is.pointer);
					cf.invokeStatic("l2j/runtime/Memory/store32(II)V");
					break;
				}
				case Ret: {
					procExitHandler(cf, totalBytesAllocated);

					InstructionRet ir = (InstructionRet) insn;
					loadValue(cf, ir.value);
					cf.returnInteger();
					break;
				}
				default:
					continue;
				// throw new UnsupportedOperationException("Unknown operation: " + insn.type);
				}
			}
		}

		cf.endMethod();
		cf.dump();
		cf.write();
	}
}

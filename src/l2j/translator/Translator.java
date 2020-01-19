package l2j.translator;

import l2j.module.Module;
import l2j.module.function.Function;

public class Translator {
	public Module m;
	
	/**
	 * LLVM module to Java bytecode translator
	 * @param m
	 */
	public Translator(Module m) {
		this.m = m;
	}
	
	/**
	 * Translate a single function to Java bytecode
	 */
	public void translateFunction(Function f) {
		TranslatedFunction tf = new TranslatedFunction();
		
	}
}

package l2j.translator;

import java.util.HashMap;

public class TranslatedFunction {
	public static class LocalVariable {
		public boolean pointerized;
		public String name;
	}
	public HashMap<String, LocalVariable> lvarInfo = new HashMap<String, LocalVariable>();
	public TranslatedFunction() {
		
	}
}

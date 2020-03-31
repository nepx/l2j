package l2j.runtime;

import java.util.HashMap;

public final class BuiltinFunctions {
	public static HashMap<String, String> list = new HashMap<String, String>();
	static {
		list.put("puts", "l2j/runtime/functions/libc/io/");
	};
}

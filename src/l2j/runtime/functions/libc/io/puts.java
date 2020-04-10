package l2j.runtime.functions.libc.io;

import l2j.runtime.Memory;

public class puts {
	public static int exec(int argptr) {
		System.out.print(Memory.readString(argptr));
		return 0;
	}
}

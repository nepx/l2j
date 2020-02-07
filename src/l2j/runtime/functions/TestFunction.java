package l2j.runtime.functions;

import l2j.runtime.FunctionImpl;

public class TestFunction extends FunctionImpl {
	
	public static final FunctionImpl __this = new TestFunction();
	
	public int call(int[] params) {
		System.out.println("Hello, world!");
		return 0;
	}

}

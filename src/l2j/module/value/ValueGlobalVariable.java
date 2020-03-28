package l2j.module.value;

import l2j.module.GlobalVariable;

public class ValueGlobalVariable extends Value {
	
	public String name;
	public GlobalVariable global;
	public ValueGlobalVariable(String s, GlobalVariable gv) {
		super(ValueType.GlobalVariable);
		name = s;
		global = gv;
	}

	@Override
	public String toString() {
		return "@" + name;
	}

	public char getJavaSignatureType() {
		return 'I';
	}
	
	public void write(byte[] out, int pos) {
		throw new IllegalStateException("todo");
	}

	public int getAddress() {
		return global.addr;
	}
}

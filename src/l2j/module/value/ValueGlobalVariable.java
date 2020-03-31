package l2j.module.value;

import l2j.module.GlobalVariable;
import l2j.module.types.Type;

public class ValueGlobalVariable extends Value {

	public String name;
	public GlobalVariable global;

	public ValueGlobalVariable(String s, GlobalVariable gv) {
		super(ValueType.GlobalVariable);
		name = s;
		global = gv;
		if (gv != null) // This can happen with functions
			llvmType = gv.type;
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

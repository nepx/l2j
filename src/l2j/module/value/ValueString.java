package l2j.module.value;

import l2j.lexer.token.TokenString;
import l2j.module.types.Type;

public class ValueString extends Value {
	public TokenString value;
	public ValueString(TokenString s) {
		super(ValueType.String);
		this.value = s;
		this.llvmType = Type.I8P;
	}
	
	public String toString() {
		return value.data;
	}

	public char getJavaSignatureType() {
		return 'I';
	}
	
	public void write(byte[] out, int pos) {
		throw new IllegalStateException("todo");
	}
}

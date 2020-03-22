package l2j.module.value;

import l2j.lexer.token.TokenString;

public class ValueString extends Value {
	public TokenString value;
	public ValueString(TokenString s) {
		super(ValueType.String);
		this.value = s;
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

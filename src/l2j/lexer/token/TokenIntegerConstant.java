package l2j.lexer.token;

public class TokenIntegerConstant extends Token{
	public int value;
	public TokenIntegerConstant(int value) {
		super(TokenType.IntegerConstant);
		this.value = value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}
}

package l2j.lexer.token;

public class TokenInteger extends Token {
	
	public int width;
	public TokenInteger(int width) {
		super(TokenType.Integer);
		this.width = width;
	}
	
	public String toString() {
		return "i" + width;
	}
}

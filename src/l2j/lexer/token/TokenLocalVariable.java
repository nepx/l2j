package l2j.lexer.token;

public class TokenLocalVariable extends Token {
	public String name;
	public TokenLocalVariable(String name) {
		super(TokenType.LocalVariable);
		this.name = name;
	}
	
	public String toString() {
		return "%" + name;
	}
}

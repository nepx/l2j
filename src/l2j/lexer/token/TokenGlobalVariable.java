package l2j.lexer.token;

public class TokenGlobalVariable extends Token {
	public String name;
	public TokenGlobalVariable(String name) {
		super(TokenType.GlobalVariable);
		this.name = name;
	}
	
	public String toString() {
		return "@" + name;
	}
}

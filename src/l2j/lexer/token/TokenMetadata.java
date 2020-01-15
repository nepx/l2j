package l2j.lexer.token;

public class TokenMetadata extends Token {
	public String name;
	public TokenMetadata(String name) {
		super(TokenType.Metadata);
		this.name = name;
	}
	public String toString() {
		return "!" + name;
	}
}

package l2j.lexer.token;

public class TokenAttributeGroup extends Token {
	public int id;
	public TokenAttributeGroup(int x) {
		super(TokenType.AttributeGroup);
		id = x;
	}
	
	public String toString() {
		return "#" + id;
	}
}

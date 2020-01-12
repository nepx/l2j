package l2j.lexer.token;

public abstract class Token {
	public TokenType type;
	public Token(TokenType t) {
		type = t;
	}
	
	public abstract String toString();
	
	// Set by the Lexer class after it has decoded a token.
	public int row, col;
}

package l2j.lexer.token;

/**
 * Any kind of special, non alpha-numeric character (i.e. = + , - etc.)
 * @author jkim13
 *
 */
public class TokenSymbol extends Token {
	
	public char symbol;
	public TokenSymbol(char sym, TokenType t) {
		super(t);
		symbol = sym;
	}
	
	public String toString() {
		if(type == TokenType.DotDotDot) return "...";
		return String.valueOf(symbol);
	}
}

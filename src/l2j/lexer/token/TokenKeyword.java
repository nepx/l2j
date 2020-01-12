package l2j.lexer.token;

public class TokenKeyword extends Token {
	
	public String kw;
	public Keyword kwe;
	public TokenKeyword(String kw, Keyword kwe) {
		super(TokenType.Keyword);
		this.kw = kw;
		this.kwe = kwe;
	}
	
	public String toString() {
		return kw;
	}
}

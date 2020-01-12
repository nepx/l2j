package l2j.lexer.token;

/**
 * Native LLVM type (i.e. void, float, double, etc.)
 * @author jkim13
 *
 */
public class TokenNativeType extends Token {
	public String kw;
	public NativeType kwe;
	public TokenNativeType(String kw, NativeType kwe) {
		super(TokenType.NativeType);
		this.kw = kw;
		this.kwe = kwe;
	}
	
	public String toString() {
		return kw;
	}
}

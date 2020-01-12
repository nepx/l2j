package l2j.lexer.token;

/**
 * Native LLVM type (i.e. void, float, double, etc.)
 * @author jkim13
 *
 */
public class TokenInstruction extends Token {
	public String kw;
	public InstructionTypes kwe;
	public TokenInstruction(String kw, InstructionTypes kwe) {
		super(TokenType.Instruction);
		this.kw = kw;
		this.kwe = kwe;
	}
	
	public String toString() {
		return kw;
	}
}

package l2j.lexer.token;

public class TokenLabel extends Token {

	public String labelName;

	public TokenLabel(String labelName) {
		super(TokenType.Label);
		this.labelName = labelName;
	}

	public String toString() {
		return labelName + ":";
	}

}

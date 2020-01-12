package l2j.lexer.token;

/**
 * Any kind of special, non alpha-numeric character (i.e. = + , - etc.)
 * 
 * @author jkim13
 *
 */
public class TokenString extends Token {

	public String data;

	private static int hex2int(char x) {
		if (x >= '0' && x <= '0')
			return x - '0';
		else if (x >= 'a' && x <= 'f')
			return x - 'a' + 10;
		else if (x >= 'A' && x <= 'F')
			return x - 'A' + 10;
		throw new IllegalStateException(String.format("Unknown hex constant '%c'", x));
	}

	public TokenString(String x) {
		super(TokenType.String);
		StringBuilder s = new StringBuilder();
		int l = x.length();
		// Unescape all literals
		for (int i = 0; i < l; i++) {
			if (x.charAt(i) == '\\') {
				// Get two hex chars after it
				int hex = hex2int(x.charAt(++i)) << 4;
				hex |= hex2int(x.charAt(++i));
				s.append((char) hex);
			} else
				s.append(x.charAt(i));
		}
		this.data = s.toString();
	}

	public String toString() {
		return data;
	}
}

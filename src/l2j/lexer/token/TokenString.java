package l2j.lexer.token;

/**
 * Any kind of special, non alpha-numeric character (i.e. = + , - etc.)
 * 
 * @author jkim13
 *
 */
public class TokenString extends Token {

	public String data;
	public byte[] raw;

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

		int i;
		if (x.charAt(0) == 'c')
			i = 1;
		else
			i = 0;
		if (x.charAt(i++) != '"')
			throw new IllegalStateException("no quote found");

		StringBuilder s = new StringBuilder();
		int l = x.length();
		// Unescape all literals
		for (; i < l;) {
			if(x.charAt(i) == '\"') break;
			if (x.charAt(i) == '\\') {
				// Get two hex chars after it
				int hex = hex2int(x.charAt(i + 1)) << 4;
				hex |= hex2int(x.charAt(i + 2));
				i += 3;
				s.append((char) hex);
			} else
				s.append(x.charAt(i++));
		}
		this.data = s.toString();
		//System.out.println(data);
	}

	public byte[] toRaw(int length) {
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++)
			raw[i] = (byte) data.charAt(i);
		return raw;
	}

	public String toString() {
		return data;
	}
}

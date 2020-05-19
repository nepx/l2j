package l2j.module.function.instruction;

import l2j.lexer.Lexer;
import l2j.lexer.token.Keyword;
import l2j.lexer.token.Token;
import l2j.lexer.token.TokenKeyword;
import l2j.lexer.token.TokenType;
import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionAdd extends Instruction {

	public boolean nsw, nuw;
	public Type type;

	public InstructionAdd(boolean nsw, boolean nuw, Type type, Value op1, Value op2) {
		super(InstructionType.Add, 2);
		this.nsw = nsw;
		this.nuw = nuw;
		this.type = type;
		this.operands[IDX_OP1] = op1;
		this.operands[IDX_OP2] = op2;
	}

	public static final int IDX_OP1 = 0, IDX_OP2 = 1;

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("add ");
		if (nuw)
			s.append("nuw ");
		if (nsw)
			s.append("nsw ");
		s.append(type.toString());
		s.append(this.operands[IDX_OP1].toString());
		s.append(", ");
		s.append(this.operands[IDX_OP2].toString());
		return s.toString();
	}

	public boolean isTerminator() {
		return false;
	}

	public Type resultType() {
		return type;
	}

	public static int parsen_w(Lexer l) {
		int res = 0;
		Token t = l.lex();
		if (t.type == TokenType.Keyword) {
			do {
				TokenKeyword kw = (TokenKeyword)t;
				if(kw.kwe == Keyword.NSW) res |= 2;
				else if(kw.kwe == Keyword.NUW) res |= 1;
				else {
					l.unlex();
					break;
				}
				t=l.lex();
			} while (t.type == TokenType.Keyword);
		}
		return res;
	}
}

package l2j.module.function.instruction;

import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionIcmp extends Instruction {
	public int cond;
	public Type type;
	public InstructionIcmp(int cond, Type type, Value op1, Value op2) {
		super(InstructionType.Icmp, 2);
		operands[0] = op1;
		operands[1] = op2;
		this.cond = cond;
		this.type = type;
	}

	public static final int COND_EQ = 0;
	public static final int COND_NE = 1;
	public static final int COND_UGT = 2;
	public static final int COND_UGE = 3;
	public static final int COND_ULT = 4;
	public static final int COND_ULE = 5;
	public static final int COND_SGT = 6;
	public static final int COND_SGE = 7;
	public static final int COND_SLT = 8;
	public static final int COND_SLE = 9;
	
	public static final String[] condnames = new String[] {
		"eq",
		"ne",
		"ugt",
		"uge",
		"ult",
		"ule",
		"sgt",
		"sge",
		"slt",
		"sle"
	};
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("icmp ");
		sb.append(condnames[cond]);
		sb.append(" ");
		sb.append(type);
		sb.append(" ");
		sb.append(operands[0]);
		sb.append(", ");
		sb.append(operands[1]);
		return sb.toString();
	}

	public boolean isTerminator() {
		return false;
	}

	public Type resultType() {
		return type; // ?
	}
}

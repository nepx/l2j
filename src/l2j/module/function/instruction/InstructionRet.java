package l2j.module.function.instruction;

import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionRet extends Instruction{
	public Type type;
	public Value value;
	public InstructionRet(Type t, Value v) {
		super(InstructionType.Ret);
		type = t;
		value = v;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ret ");
		b.append(type.toString());
		b.append(", ");
		b.append(value.toString());
		return b.toString();
	}

	public boolean isTerminator() {
		return true;
	}

	public Type resultType() {
		return null;
	}

}

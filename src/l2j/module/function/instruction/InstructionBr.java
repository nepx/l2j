package l2j.module.function.instruction;

import l2j.module.function.Label;
import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionBr extends Instruction {

	public boolean hasCond;
	public Label ifTrue, ifDefault;
	public Value cond;
	public InstructionBr(boolean hasCond, Value cond, Label a, Label b) {
		super(InstructionType.Br, 1);
		this.hasCond = hasCond;
		if(hasCond) {
			this.cond = cond;
			ifTrue = a;
			ifDefault = b;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("br ");
		if(hasCond) {
			sb.append("i1 ");
			sb.append(cond);
			sb.append(", label ");
			sb.append(ifTrue);
			sb.append(", ");
		}
		sb.append("label ");
		sb.append(ifDefault);
		
		return sb.toString();
	}

	public boolean isTerminator() {
		return true;
	}

	public Type resultType() {
		return Type.Void;
	}

}

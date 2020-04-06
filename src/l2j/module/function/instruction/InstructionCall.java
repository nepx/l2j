package l2j.module.function.instruction;

import java.util.ArrayList;

import l2j.module.attributes.Attribute;
import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionCall extends Instruction {

	public Type returnType;
	public ArrayList<Value> args;
	public static final int IDX_FPTR = 0;
	public static final int IDX_ARGS = 1;

	public InstructionCall(Type returnType, Value fnptrval, ArrayList<Value> args) {
		super(InstructionType.Call, 1 + args.size());
		int argsz = args.size();
		operands[IDX_FPTR] = fnptrval;
		for (int i = 0; i < argsz; i++)
			operands[IDX_ARGS + i] = args.get(i);
		this.returnType = returnType;
		this.args = args;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("call ");
		return s.toString();
	}

	@Override
	public boolean isTerminator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type resultType() {
		// TODO Auto-generated method stub
		return null;
	}
}

package l2j.module.function.instruction;

import java.util.ArrayList;

import l2j.module.attributes.Attribute;
import l2j.module.types.Type;
import l2j.module.value.Value;

public class InstructionCall extends Instruction {

	public Type returnType;
	public Value fnptrval;
	public ArrayList<Value> args;

	public InstructionCall(Type returnType, Value fnptrval, ArrayList<Value> args) {
		super(InstructionType.Call);
		this.returnType = returnType;
		this.fnptrval = fnptrval;
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

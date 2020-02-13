package l2j.module.function.instruction;

import java.util.ArrayList;

import l2j.module.attributes.Attribute;
import l2j.module.types.Type;

public class InstructionCall extends Instruction {
	public ArrayList<Attribute> attributes;

	public InstructionCall() {
		super(InstructionType.Call);

	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("call ");
		for (int i = 0; i < attributes.size(); i++)
			s.append(attributes.get(i).toString());

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

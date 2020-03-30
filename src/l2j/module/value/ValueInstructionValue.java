package l2j.module.value;

import l2j.module.function.instruction.Instruction;

public class ValueInstructionValue extends Value {
	public Instruction i;

	public ValueInstructionValue(Instruction i) {
		super(ValueType.Instruction);
		this.i = i;
	}

	@Override
	public String toString() {
		return i.toString();
	}

	@Override
	public char getJavaSignatureType() {
		return i.resultType().getJavaSignatureType();
	}

	public void write(byte[] out, int pos) {
		throw new IllegalStateException("cannot write instruction");

	}

}

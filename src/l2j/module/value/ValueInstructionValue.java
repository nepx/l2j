package l2j.module.value;

import l2j.module.function.instruction.Instruction;

public class ValueInstructionValue extends Value {
	public Instruction i;
	public int idx;

	public ValueInstructionValue(Instruction i, int idx) {
		super(ValueType.Instruction);
		this.i = i;
		this.idx = idx;
		
		llvmType = i.resultType();
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

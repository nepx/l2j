package l2j.module.function.instruction;

import l2j.module.function.LocalVariable;
import l2j.module.types.Type;
import l2j.module.value.Value;

public abstract class Instruction {
	public InstructionType type;
	public Value[] operands;

	public Instruction(InstructionType type, int operandCount) {
		this.type = type;
		operands = new Value[operandCount];
	}

	/**
	 * The destination of the operation (where does the result go?)
	 */
	public LocalVariable destination;
	/**
	 * Instead of allocating a local variable, can we keep this on the stack?
	 */
	public boolean stackable;

	public abstract String toString();

	public abstract boolean isTerminator();

	public abstract Type resultType();
}

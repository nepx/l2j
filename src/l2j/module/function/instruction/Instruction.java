package l2j.module.function.instruction;

import l2j.module.types.Type;

public abstract class Instruction {
	public InstructionType type;
	public Instruction(InstructionType type) {
		this.type = type;
	}
	
	/**
	 * The destination of the operation (where does the result go?)
	 */
	public String destination;
	
	public abstract String toString();
	public abstract boolean isTerminator();
	public abstract Type resultType();
}

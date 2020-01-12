package l2j.module.function;

import java.util.ArrayList;

import l2j.module.function.instruction.Instruction;

public class BasicBlock {
	public int id;
	public ArrayList<Instruction> instructions = new ArrayList<Instruction>();
}

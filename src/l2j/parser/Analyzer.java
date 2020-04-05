package l2j.parser;

import java.util.ArrayList;
import java.util.Map;

import l2j.module.function.BasicBlock;
import l2j.module.function.Function;
import l2j.module.function.LocalVariable;
import l2j.module.function.instruction.*;
import l2j.module.value.*;

/**
 * Analyze individual functions and try to pre-optimize them. Strategies include
 * the following: - Eliminate local variables where possible (try to keep
 * everything on the stack) -
 * 
 * @author jkim13
 *
 */
public class Analyzer {
	public static void analyze(Function f) {
		int listlen = f.insns.size(), base = 0;
		for (int x = 0; x < listlen; x++) {
			Instruction i = f.insns.get(x);
			loop1: for (int y = 0; y < i.operands.length; y++) {
				if (i.operands[y].type == ValueType.LocalVariable) {
					// Look back and see if we have a corresponding lvarid
					int z = x - 1;
					LocalVariable lv = ((ValueLocalVariable) i.operands[y]).backingVar;
					while (z >= base) {// Look through all instructions until the fence
						Instruction i2 = f.insns.get(z);
						if (i2.destination == null) { // Ignore if no destination
							z--;
							continue;
						}
						if (lv.equals(i2.destination)) { // If they are the same, then replace operand with ctual instruction
							i.operands[y] = new ValueInstructionValue(i2, z);
							continue loop1;
						}
						z--;
					}
				}
			}
			if (i.destination != null) {
				// Check if our variable has a destination
			}

			// Check if we can have a reordering constraint -- anything that modifies
			// external program state.
			if (ReorderingConstraint.contains(i.type)) {
				// If so, we cannot go BEHIND this instruction
				base = x;
			}
		}
	}

	public static final ArrayList<InstructionType> ReorderingConstraint = new ArrayList<InstructionType>();
	static {
		ReorderingConstraint.add(InstructionType.Store);
	};
}

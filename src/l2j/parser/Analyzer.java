package l2j.parser;

import java.util.ArrayList;
import java.util.Map;

import l2j.module.function.BasicBlock;
import l2j.module.function.Function;
import l2j.module.function.instruction.*;

/**
 * Analyze individual functions and try to pre-optimize them. Strategies include the following:
 *  - Eliminate local variables where possible (try to keep everything on the stack)
 *  - 
 * @author jkim13
 *
 */
public class Analyzer {
	public static void analyze(Function f) {
		for (Map.Entry<String, BasicBlock> entry : f.blocks.entrySet()) {
			BasicBlock b = entry.getValue();
			int listlen = b.instructions.size();
			for(int x=0;x<listlen;x++) {
				Instruction i = b.instructions.get(x);
				
				// Check if we can have a reordering constraint -- anything that modifies external program state.
				if(i.destination != null) {
					// Check if our variable has a destination
				}
			}
		}
	}
	
	public static final ArrayList<InstructionType> ReorderingConstraint = new ArrayList<InstructionType>();
	static {
		ReorderingConstraint.add(InstructionType.Store);
	};
}

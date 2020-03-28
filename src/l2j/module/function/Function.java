package l2j.module.function;

import java.util.ArrayList;
import java.util.HashMap;

import l2j.module.Linkage;
import l2j.module.Module;
import l2j.module.attributes.*;
import l2j.module.types.Type;

public class Function extends Callable{
	// LLVM can have named basic blocks, so we must index by name
	public HashMap<String, BasicBlock> blocks = new HashMap<String, BasicBlock>();
	public HashMap<String, LocalVariable> lvars = new HashMap<String, LocalVariable>();
	public Module parentModule;

	private int internalNumberingID;
	
	private int internalLvarNumberingID = 0;
	public int getLocalVariableID() {
		return internalLvarNumberingID++;
	}

	public String getTempName() {
		return Integer.toString(internalNumberingID++);
	}
}

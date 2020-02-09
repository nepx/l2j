package l2j.module;

import l2j.module.types.Type;
import l2j.module.value.Value;

public class GlobalVariable {
	public Linkage visibility = new Linkage();
	public Type type;
	public Value initializerValue;
}

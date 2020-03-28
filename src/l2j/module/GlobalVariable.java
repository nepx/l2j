package l2j.module;

import l2j.module.data.ArrayData;
import l2j.module.data.DataSectionEntry;
import l2j.module.types.*;
import l2j.module.value.*;

public class GlobalVariable {
	public Linkage visibility = new Linkage();
	public Type type;
	public Value initializerValue;
	public int addr;
	
	public int align;

	public DataSectionEntry getDataSectionEntry() {
		switch (initializerValue.type) {
		case String:{
			if(type.type != TypeType.Array) throw new IllegalStateException("type of string is not char array\n");
			ArrayType at = (ArrayType)type;
			return new ArrayData((ValueString)initializerValue, at.count);
		}
		default:
			throw new UnsupportedOperationException("TODO: type " + initializerValue.type);
		}
	}
}

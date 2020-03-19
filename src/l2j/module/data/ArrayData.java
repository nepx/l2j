package l2j.module.data;

import l2j.module.types.Type;
import l2j.module.value.Value;
import l2j.module.value.ValueString;

public class ArrayData extends DataSectionEntry {
	private Type type;
	private byte[] data;
	private Value[] values;

	public ArrayData(ValueString str, int length) {
		data = str.value.toRaw(length);
		type = Type.I8;
	}

	public int align() {
		int align = type.getPreferredAlignment();
		return align < 4 ? 4 : align;
	}

	public int size() {
		if (data != null)
			return data.length;
		else // TODO: precalculate this?
			return values.length * type.getSize();
	}

	public void outputBytes(byte[] output, int offset) {
		if (data != null) {
			System.arraycopy(data, 0, output, offset, data.length);
		} else {
			// TODO
		}
	}

}

package l2j.module.data;

public abstract class DataSectionEntry {
	public abstract int align();
	public abstract int size();
	public abstract void outputBytes(byte[] output, int offset);
}

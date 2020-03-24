package l2j.module.data;

public abstract class DataSectionEntry {
	public abstract int align();

	public abstract int size();

	public abstract void outputBytes(byte[] output, int offset);

	/**
	 * Address of data in memory
	 */
	public int addr = -1;
}

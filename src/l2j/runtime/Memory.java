package l2j.runtime;

/**
 * @author jkim13
 *
 */
public class Memory {
	public static final int MEMORY_SIZE = 32 * 1024 * 1024;
	public static final int[] memory = new int[MEMORY_SIZE >> 2];
}

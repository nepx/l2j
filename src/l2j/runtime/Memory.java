package l2j.runtime;

/**
 * @author jkim13
 *
 */
public class Memory {
	public static final int MEMORY_SIZE = 32 * 1024 * 1024;
	public static final int[] memory = new int[MEMORY_SIZE >> 2];

	/**
	 * Store a 32-bit aligned dword
	 * 
	 * @param addr Address
	 * @param data Data
	 */
	public static void storeI32Aligned(int addr, int data) {
		memory[addr >> 2] = data;
	}

	public static void storeI32(int addr, int data) {
		int lowbits = addr & 3;
		if (lowbits != 0) { // Unaligned
			int aligned_addr = addr >> 2,
					// Number of bytes to shift
					to_shift = lowbits << 3,
					// Inverse of quanity of bits to shift
					shift_high = 32 - to_shift,
					// High mask
					mask_high = -1 << to_shift,
					// Low mask
					mask_low = ~mask_high;
			int[] local_ref = memory;
			local_ref[aligned_addr] = (local_ref[aligned_addr] & mask_low | data << to_shift);
			aligned_addr++;
			local_ref[aligned_addr] = (local_ref[aligned_addr] & mask_high | data >> shift_high);
		} else // Aligned
			memory[addr >> 2] = data;
	}
}

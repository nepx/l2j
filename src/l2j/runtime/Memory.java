package l2j.runtime;

/**
 * Memory implementation. Handles a flat heap address space. Handles some parts
 * of the stack.
 * 
 * @author jkim13
 *
 */
public class Memory {
	public static final int MEMORY_SIZE = 32 * 1024 * 1024;
	public static final int STATIC_MEMORY_BASE = 256;
	public static final int[] memory = new int[MEMORY_SIZE >> 2];

	/**
	 * The stack pointer. Like x86 stacks, it grows downwards.
	 */
	private static int esp = 0;

	/**
	 * Allocate some memory on the stack.
	 * 
	 * @param size
	 * @return
	 */
	public static int alloca(int size) {
		esp -= size;
		return esp;
	}

	/**
	 * Deallocate some memory from stack
	 * 
	 * @param size
	 * @return
	 */
	public static void dealloca(int size) {
		esp += size;
	}

	public static int load8(int addr) {
		return memory[addr >> 2] >> ((addr & 3) << 3) & 0xFF;
	}

	/**
	 * Store a 32-bit aligned dword
	 * 
	 * @param addr Address
	 * @param data Data
	 */
	public static void store32Aligned(int addr, int data) {
		memory[addr >> 2] = data;
	}

	public static void store32(int addr, int data) {
		int lowbits = addr & 3;
		if (lowbits != 0) { // Unaligned version
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

	public static String readString(int addr) {
		StringBuilder b = new StringBuilder();
		while (true) {
			int val = load8(addr++);
			if (val == 0)
				break;
			b.append((char) val);
		}
		return b.toString();
	}
}

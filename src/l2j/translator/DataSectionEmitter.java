package l2j.translator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.omg.CORBA_2_3.portable.OutputStream;

import l2j.module.Module;
import l2j.module.data.DataSectionEntry;
import l2j.runtime.Memory;

public class DataSectionEmitter {
	private Module m;
	private DataSectionEntry[] entries;

	public DataSectionEmitter(Module m) {
		this.m = m;

		entries = new DataSectionEntry[m.data.size()];
		int i = 0;
		for (Map.Entry<String, DataSectionEntry> entry : m.data.entrySet())
			entries[i++] = entry.getValue();
	}

	/**
	 * Precalculate size of buffer required to hold all data
	 * 
	 * @return Size, in bytes, of all static data
	 */
	public int precalcBufferSize() {
		int total = 0;
		for (int i = 0; i < entries.length; i++)
			total += entries[i].size();
		return total;
	}

	/**
	 * Actually write the bytes to destination array
	 * 
	 * @param dest
	 */
	public void generate(byte[] dest) {
		int ptr = 0, ptrbias = Memory.STATIC_MEMORY_BASE;
		for (int i = 0; i < entries.length; i++) {
			entries[i].outputBytes(dest, ptr);
			entries[i].addr = ptr + ptrbias;
			ptr += entries[i].size();
		}

	}

	public int writeFile(String dataDestPath, byte[] data) {
		try {
			File f = new File(dataDestPath);
			FileOutputStream o = new FileOutputStream(f);
			o.write(data);
			o.close();
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}
}

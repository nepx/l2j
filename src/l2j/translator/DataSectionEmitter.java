package l2j.translator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.omg.CORBA_2_3.portable.OutputStream;

import l2j.module.GlobalVariable;
import l2j.module.Module;
import l2j.module.data.DataSectionEntry;
import l2j.runtime.Memory;

public class DataSectionEmitter {
	private Module m;
	private DataSectionEntry[] entries;
	private GlobalVariable[] globals;

	public DataSectionEmitter(Module m) {
		this.m = m;

		entries = new DataSectionEntry[m.globals.size()];
		globals = new GlobalVariable[entries.length];
		int i = 0;
		for (Map.Entry<String, GlobalVariable> entry : m.globals.entrySet()) {
			entries[i] = entry.getValue().getDataSectionEntry();
			globals[i++] = entry.getValue();
		}
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
		return (total + 3) & ~3; // align to dword boundary
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
			System.out.println("here: " + entries[i]);
			globals[i].addr = entries[i].addr = ptr + ptrbias;
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

package l2j.runtime;

import java.io.File;
import java.io.FileInputStream;

public class DataSectionLoader {
	public static int loadMemoryImage(String path) {
		FileInputStream fileInputStream = null;
		try {
			File file = new File(path);
			fileInputStream = new FileInputStream(file);
			byte[] contents = new byte[(int) file.length()];
			fileInputStream.read(contents);
			fileInputStream.close();
			if ((contents.length & 3) != 0) {
				System.err.println("Length of memory image file must be aligned to dword");
				return -1;
			}

			int len = contents.length /* >> 2 */;
			for (int i = 0; i < len; i += 4) {
				int val = (int) contents[i] | ((int) contents[i + 1]) << 8 | ((int) contents[i + 2]) << 16
						| ((int) contents[i + 3]) << 24;
				Memory.memory[(i + Memory.STATIC_MEMORY_BASE) >> 2] = val;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}

package l2j.runtime;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Dynamically loads modules from class files.
 * 
 * @author jkim13
 *
 */
public class DynamicMethodLoader {
	private String basepath;
	private ClassLoader classLoader;

	/**
	 * Initialize method loader with a given base path
	 * 
	 * @param basepath
	 */
	public DynamicMethodLoader(String basepath) {
		this.basepath = basepath;
		try {
			File f = new File(basepath);
			URL[] urls = new URL[] {f.toURI().toURL()};
			classLoader = new URLClassLoader(urls);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a file from URL
	 * @param classId
	 * @return
	 */
	public FunctionImpl loadInstanceURL(String classId) {
		try {
			Class clz = classLoader.loadClass(classId);
			return (FunctionImpl) clz.newInstance();
		}catch(Exception e) {
			e.printStackTrace();
			return loadInstance(classId);
		}
	}

	public FunctionImpl loadInstance(String classId) {
		try {
			Class clz = Thread.currentThread().getContextClassLoader().loadClass(basepath + classId);
			return (FunctionImpl) clz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Class clz = Class.forName(basepath + classId);
				return (FunctionImpl) clz.newInstance();
			} catch (Exception f) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

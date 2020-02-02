package l2j.runtime;

/**
 * Dynamically loads modules from class files.
 * 
 * @author jkim13
 *
 */
public class DynamicMethodLoader {
	private String basepath;

	/**
	 * Initialize method loader with a given base path
	 * 
	 * @param basepath
	 */
	public DynamicMethodLoader(String basepath) {
		this.basepath = basepath;
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

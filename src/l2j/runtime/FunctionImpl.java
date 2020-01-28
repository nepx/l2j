package l2j.runtime;

/**
 * An implementation of a function, either compiled, user-supplied, or a native routine.  
 * @author jkim13
 *
 */
public abstract class FunctionImpl {
	public abstract int call(int[] params);
}

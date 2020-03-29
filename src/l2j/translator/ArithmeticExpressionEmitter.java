package l2j.translator;

import java.util.ArrayList;

import l2j.module.value.Value;

/**
 * Computes sum of (a*b) pairs and tries to emit the most efficient Java byte
 * code sequence from them
 * 
 * @author jkim13
 *
 */
public class ArithmeticExpressionEmitter {
	/**
	 * <code>lvar * mult</code>
	 * 
	 * @author jkim13
	 *
	 */
	private class VariableExpression {
		public Value value;
		public int multi;
	};

	private VariableExpression[] arrs;
	private int counter = 0, constantTotal;

	public ArithmeticExpressionEmitter(int elts) {
		arrs = new VariableExpression[elts];
	}

	/**
	 * Add a multiplication pair to the list
	 * 
	 * @param n1
	 * @param n2
	 */
	public void addMultiplicationPair(int n1, Value n2) {
		if (n2 == null) // Sum together all the products of the constant expressions
			constantTotal += n1;
		else {
			VariableExpression ve = new VariableExpression();
			ve.value = n2;
			ve.multi = n1;
			arrs[counter++] = ve;
		}
	}

	/**
	 * Emit Java bytecode for this expression
	 * 
	 * @param cf
	 */
	public void emit(ClassFileEmitter cf, Translator t) {
		// Check whether counter is zero. If it is, then all we have to do is emit the
		// constant
		if (counter == 0)
			cf.pushInt(constantTotal);
		else {
			for (int i = 0; i < counter; i++) {
				VariableExpression ve = arrs[i];
				t.loadValue(cf, ve.value);
				if (ve.multi != 1) {
					cf.pushInt(ve.multi);
					cf.emitImul();
				}
				if (i != 0) {
					cf.emitIadd();
				}
			}
			if (constantTotal != 0) {
				cf.pushInt(constantTotal);
				cf.emitIadd();
			}
		}
	}
}

package l2j.module.types;

import java.util.ArrayList;

public class FunctionPointerType extends Type {
	public Type returnType;
	/**
	 * Mandatory arguments -- arguments that you *need* to pass to the function
	 * every time. For example, for a call of <code>i32 @printf(i8*, ...)</code>,
	 * the only mandatory argument would be the <code>i8*</code>
	 */
	public Type[] mandatoryArguments;

	public boolean varargs;

	public FunctionPointerType(Type ret, ArrayList<Type> mandatory, boolean varargs) {
		super(TypeType.FunctionPointer);
		this.returnType = ret;
		Type[] args = new Type[mandatory.size()];
		for (int i = 0; i < args.length; i++)
			args[i] = mandatory.get(i);
		this.mandatoryArguments = args;
		this.varargs = varargs;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(returnType);
		s.append(" (");
		for (int i = 0; i < mandatoryArguments.length; i++) {
			if (i != 0)
				s.append(", ");
			s.append(mandatoryArguments[i]);
		}
		if (varargs)
			s.append(", ...");
		s.append(")");
		return s.toString();
	}

	@Override
	public int getPreferredAlignment() {
		return 4;
	}

	@Override
	public char getJavaSignatureType() {
		return 'I';
	}

	@Override
	public int getSize() {
		return 4;
	}

	protected boolean internalCompare(Type t) {
		FunctionPointerType a = (FunctionPointerType) t;
		if (a.returnType.equals(returnType) && a.varargs == varargs) {
			if (a.mandatoryArguments.length != mandatoryArguments.length)
				return false;
			for (int i = 0; i < mandatoryArguments.length; i++)
				if (!mandatoryArguments[i].equals(a.mandatoryArguments[i]))
					return false;
			return true;
		}
		return false;
	}
}

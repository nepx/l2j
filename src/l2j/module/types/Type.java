package l2j.module.types;

public abstract class Type {
	public TypeType type;

	public Type(TypeType t) {
		type = t;
	}

	public abstract String toString();
	
	public boolean isVector() {
		return false;
	}

	public abstract int getPreferredAlignment();

	public abstract char getJavaSignatureType();

	public abstract int getSize();
	
	protected abstract boolean internalCompare(Type t);

	/**
	 * Can we use the <code>getelementptr</code> instruction?
	 * 
	 * @return can we?
	 */
	public boolean canGEP() {
		return false;
	}

	public boolean equals(Type t) {
		return this.type == t.type && internalCompare(t);
	}

	public static final IntegerType I1 = new IntegerType(1);
	public static final IntegerType I8 = new IntegerType(8);
	public static final IntegerType I32 = new IntegerType(32);
	public static final PointerType I8P = new PointerType(I8);
}

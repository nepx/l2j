package l2j.module.types;

public abstract class ArrayOrStructType extends Type {
	public ArrayOrStructType(TypeType t) {
		super(t);
	}

	public abstract Type getElementAtIndex(int x);

	public abstract int getOffset(int x);

	@Override
	public boolean canGEP() {
		return true;
	}
}

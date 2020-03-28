package l2j.module.types;

public class ArrayType extends ArrayOrStructType {
	public Type elts;
	public int count;

	public ArrayType(Type elts, int count) {
		super(TypeType.Array);
		this.elts = elts;
		this.count = count;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		b.append(elts.toString());
		b.append(" x ");
		b.append(count);
		b.append("]");
		return b.toString();
	}

	public int getPreferredAlignment() {
		// Simply get alignment of elements
		return elts.getPreferredAlignment();
	}

	public char getJavaSignatureType() {
		return 'I';
	}

	public int getSize() {
		return elts.getSize() * count;
	}

	public Type getElementAtIndex(int x) {
		return elts;
	}

	public int getOffset(int x) {
		if (x >= count)
			System.err.printf("Reading too many bytes: arg=%d count=%d\n", x, count);
		return x * elts.getSize();
	}
}

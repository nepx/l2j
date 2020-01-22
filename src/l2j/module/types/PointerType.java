package l2j.module.types;

public class PointerType extends Type{
	public Type pointedTo;
	public PointerType(Type t) {
		super(TypeType.Pointer);
		this.pointedTo = t;
	}

	public String toString() {
		return pointedTo.toString() + "*";
	}
	
	public char getJavaSignatureType() {
		return 'I';
	}

	public int getPreferredAlignment() {
		return 4;
	}
	public int getSize() {
		return 4;
	}
}

package l2j.module.types;

public class VoidType extends Type{
	public VoidType() {
		super(TypeType.Void);
	}

	public String toString() {
		return "void";
	}
	
	public char getJavaSignatureType() {
		return 'V';
	}

	public int getPreferredAlignment() {
		return 0;
	}

	public int getSize() {
		return 0;
	}
}

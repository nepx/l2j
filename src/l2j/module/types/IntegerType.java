package l2j.module.types;

public class IntegerType extends Type {
	public int width;
	public IntegerType(int width) {
		super(TypeType.Integer);
		this.width = width;
	}
	
	public int getPreferredAlignment() {
		return (this.width + 7) >> 3;
	}
	
	public char getJavaSignatureType() {
		// TODO: larger integers?
		return 'I';
	}
	
	public String toString() {
		return "i" + width;
	}
	
	public int getSize() {
		return getPreferredAlignment();
	}
}

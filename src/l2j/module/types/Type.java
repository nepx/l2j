package l2j.module.types;

public abstract class Type {
	public TypeType type;
	public Type(TypeType t) {
		type = t;
	}
	
	public abstract String toString();
	public abstract int getPreferredAlignment();
	public abstract char getJavaSignatureType();
	public abstract int getSize();
	
	public boolean equals(Type t) {
		return this.type == t.type;
	}
}

package l2j.module.function.instruction;

import l2j.module.types.*;
import l2j.module.value.Value;

public class InstructionAlloca extends Instruction {
	public boolean inalloca;
	public Type type;
	public Type numElementsType;
	private boolean numElementsDefault, alignDefault, addrspaceDefault;
	public int align;
	public int addrspace;
	
	public static final int IDX_NELTS =0 ;

	public InstructionAlloca(boolean inalloca, Type type, Type numElementsType, Value numElements, int align,
			int addrspace) {
		super(InstructionType.Alloca, 1);
		operands[IDX_NELTS] = numElements;
		this.inalloca = inalloca;
		this.type = type;
		if (numElementsType == null) {
			this.numElementsType = new IntegerType(32);
			this.numElementsDefault = true;
		}
		if(numElements == null) 
			operands[IDX_NELTS] = Value.CONST_1;
		if (align == 0 || align >= (1 << 29)) {
			this.alignDefault = true;
			this.align = type.getPreferredAlignment();
		} else
			this.align = align;
		this.addrspace = addrspace;
		this.addrspaceDefault = addrspace == 0;
	}

	public String toString() {
		StringBuilder x = new StringBuilder();
		x.append("alloca");
		if (inalloca)
			x.append(" inalloca");
		x.append(" ");
		x.append(type.toString());

		if (!this.numElementsDefault) {
			x.append(", ");
			x.append(this.numElementsType.toString());
			x.append(" ");
			x.append(this.operands[IDX_NELTS]);
		}
		if (!this.alignDefault) {
			x.append(", align ");
			x.append(this.align);
		}
		// TODO: Append addrspace
		if(!this.addrspaceDefault) {
			// ??
		}
		return x.toString();
	}

	public boolean isTerminator() {return false;}
	public Type resultType() {return new PointerType(type); }
}

package l2j.module.function.instruction;

import l2j.module.types.*;
import l2j.module.value.*;

public class InstructionStore extends Instruction {
	public boolean atomic, isVolatile;
	public Type type, pointerType;
	public Value value, pointer;
	public int align, nontemporalIndex, invariantGroup;
	private boolean alignUnspecified;

	public InstructionStore(boolean atomic, boolean isVolatile, Type type, Value value, Type pointerType, Value pointer, int align,
			int nontemporalIndex, int invariantGroup) {
		super(InstructionType.Store);
		this.atomic = atomic;
		this.isVolatile = isVolatile;
		this.type = type;
		this.value = value;
		this.pointerType = pointerType;
		this.pointer = pointer;
		if(align == 0) {
			this.alignUnspecified = true;
			this.align = type.getPreferredAlignment();
		}
		this.align = align;
		this.nontemporalIndex = nontemporalIndex;
		this.invariantGroup = invariantGroup;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("store");
		if(atomic) s.append(" atomic");
		if(isVolatile) s.append(" volatile");
		s.append(" ");
		s.append(this.type.toString());
		s.append(" ");
		s.append(this.value.toString());
		s.append(", ");
		s.append(this.pointerType.toString());
		s.append("* ");
		s.append(this.pointer.toString());
		if(!this.alignUnspecified) {
			s.append(", ");
			s.append(this.align);
		}
		if(this.nontemporalIndex != 0) {
			s.append(", !nontemporal !");
			s.append(this.nontemporalIndex);
		}
		if(this.invariantGroup != 0) {
			s.append(", !invariant.group !");
			s.append(this.invariantGroup);
		}
		return s.toString();
	}

	public boolean isTerminator() {
		return false;
	}

	public Type resultType() {
		return new VoidType();
	}
}

package l2j.module.function;

import java.util.ArrayList;

import l2j.module.Linkage;
import l2j.module.attributes.Attribute;
import l2j.module.types.Type;

public abstract class Callable {
	public Linkage visibillity = new Linkage();

	public ArrayList<Attribute> returnAttributes = new ArrayList<Attribute>();

	public Type returnType;

	public String name;
	public ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	// The function attributes themselves
	public ArrayList<Attribute> attributess = new ArrayList<Attribute>();

	public String methodSignature;

	public String getMethodSignature() {
		return methodSignature;
	}

	public void finalize() {
		StringBuilder s = new StringBuilder();
		int len = parameters.size();
		for (int i = 0; i < len; i++)
			s.append(parameters.get(i).type.getJavaSignatureType());
		s.append("(");
		s.append(returnType.getJavaSignatureType());
		s.append(")");
	}
}

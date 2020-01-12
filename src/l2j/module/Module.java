package l2j.module;

import java.util.ArrayList;
import java.util.HashMap;

import l2j.module.attributes.Attribute;
import l2j.module.attributes.AttributeList;
import l2j.module.function.Function;

public class Module {
	public Module() {}
	
	// Target triple and datalayout
	public String targetTriple, datalayout;
	
	public ArrayList<Function> functions = new  ArrayList<Function>();
	public HashMap<Integer, AttributeList> attributes = new HashMap<Integer, AttributeList>();
}

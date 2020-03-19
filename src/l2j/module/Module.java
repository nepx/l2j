package l2j.module;

import java.util.ArrayList;
import java.util.HashMap;

import l2j.module.attributes.AttributeList;
import l2j.module.data.DataSectionEntry;
import l2j.module.function.Callable;

public class Module {
	public Module() {}
	
	// Target triple and datalayout
	public String targetTriple, datalayout;
	
	public HashMap<String, Callable> functions = new HashMap<String, Callable>();
	public HashMap<Integer, AttributeList> attributes = new HashMap<Integer, AttributeList>();
	
	public HashMap<String, DataSectionEntry> data = new HashMap<String, DataSectionEntry>();
}

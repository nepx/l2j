package l2j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import l2j.lexer.Lexer;
import l2j.module.Module;
import l2j.module.function.*;
import l2j.parser.Parser;
import l2j.runtime.DataSectionLoader;
import l2j.runtime.DynamicMethodLoader;
import l2j.runtime.FunctionImpl;
import l2j.translator.ClassFileCompiler;
import l2j.translator.DataSectionEmitter;
import l2j.translator.Translator;

public class Main {
	private static void help() {
		System.err.println("l2j : LLVM-to-Java compiler");
		System.err.println("Compiler options:");
		System.err.println(" --help: Help");
		System.exit(1);
	}

	public static void main(String[] args) {
		String input = null;
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "--help":
				help();
				break;
			default:
				input = args[i];
				break;
			}
		}
		if (input == null) {
			System.err.println("No input files specified");
			System.exit(1);
		}
		Lexer l = Lexer.loadFromFile(input);
		Parser p = new Parser(l);
		Module m = new Module();
		System.out.println("Lexing and parsing...");
		p.parse(m);
		
		System.out.println("Generating data section...");
		DataSectionEmitter dse = new DataSectionEmitter(m);
		byte[] dataSection = new byte[dse.precalcBufferSize()];
		dse.generate(dataSection);
		dse.writeFile("data.bin", dataSection);

		String basename = "bin/";

		Translator t = new Translator(m, basename);
		int flen = m.functions.size();
		System.out.println("Generating Jasmin assembly...");
		for (Map.Entry<String, Callable> entry : m.functions.entrySet()) {
			Callable c = entry.getValue();
			if (c instanceof Function)
				t.translateFunction((Function) c);
		}

		System.out.println("Compiling Jasmin assembly...");
		ClassFileCompiler comp = new ClassFileCompiler(basename);
		for (Map.Entry<String, Callable> entry : m.functions.entrySet()) {
			Callable c = entry.getValue();
			if (c instanceof Function)
				comp.compile((Function) c);
		}

		// Now try running the file
		
		// Load our data file
		DataSectionLoader.loadMemoryImage("data.bin");

		DynamicMethodLoader dml2 = new DynamicMethodLoader("");
		FunctionImpl test2 = dml2.loadInstance("l2j.generated.main");
		System.out.println(test2.call(0));
	}
}

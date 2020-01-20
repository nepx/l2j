package l2j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import l2j.lexer.Lexer;
import l2j.module.Module;
import l2j.parser.Parser;
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
		for(int i=0;i<args.length;i++) {
			switch(args[i]) {
			case "--help":
				help();
				break;
			default:
				input = args[i];
				break;
			}
		}
		if(input == null) {
			System.err.println("No input files specified");
			System.exit(1);
		}
		Lexer l = Lexer.loadFromFile(input);
		Parser p = new Parser(l);
		Module m = new Module();
		p.parse(m);
		
		String basename = null;
		// Open file stream
		if(input.contains("."))basename = input.substring(0, input.lastIndexOf('.')) + ".jasmin";
		else basename = input + ".jasmin";
		
		BufferedWriter writer;
		Translator t;
		try {
			File file = new File(basename);
			FileOutputStream fs = new FileOutputStream(file);
			OutputStreamWriter ow = new OutputStreamWriter(fs);
			writer = new BufferedWriter(ow);
			t = new Translator(m, writer);
			
			int flen = m.functions.size();
			for(int i=0;i<flen;i++)
				t.translateFunction(m.functions.get(i));
		} catch(IOException e) {
			System.out.println("Unable to produce Jasmin assembly file");
			e.printStackTrace();
			System.exit(1);
		}
	}
}

package l2j.translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import l2j.module.function.Function;

public class ClassFileCompiler {
	private String basename;

	public ClassFileCompiler(String basename) {
		this.basename = basename;
	}

	public void compile(Function f) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		System.out.printf("%s %s %s %s %s %s\n", "java", "-jar", "jasmin.jar", "-d", basename, basename + f.name + ".jasmin");
		processBuilder.command("java", "-jar", "jasmin.jar", "-d", basename, basename + f.name + ".jasmin");
		try {
			Process process = processBuilder.start();
			
			// Read stdout
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exit = process.waitFor();
            if(exit != 0) throw new IllegalStateException("Jasmin assembler failed: " + exit);
		}catch(Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Jasmin assembler failed");
		}
	}
}

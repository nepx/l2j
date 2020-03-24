package l2j.lexer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import l2j.lexer.token.*;

public class Lexer {
	private byte[] data;
	private int pos, startPos;

	private int lastTokenStart;

	/**
	 * Moves the lexer position back before the last token was parsed.
	 */
	public void unlex() {
		pos = lastTokenStart;
	}

	/**
	 * Returns char at current position and advances one character forward
	 * 
	 * @return
	 */
	private char next() {
		if (pos >= data.length)
			return 0xFFFF;
		char next = (char) data[pos++];
		return next;
	}

	/**
	 * Returns char at the previous position and rewinds the position
	 * 
	 * @return
	 */
	private char prev() {
		char next = (char) data[--pos];
		return next;
	}

	/**
	 * Mark starting position of token so that we can extract text later
	 */
	private void start(int offset) {
		startPos = pos + offset;
	}

	/**
	 * Rewind to starting position
	 */
	private void rewind() {
		pos = startPos;
	}

	/**
	 * Skips to the next character
	 */
	private void skip() {
		pos++;
	}

	private void back() {
		pos--;
	}

	private char at() {
		return (char) data[pos];
	}

	private char at(int offset) {
		return (char) data[pos + offset];
	}

	public Lexer(byte[] input) {
		data = input;
	}

	private boolean tokenEnd(char chr) {
		return chr == 0 || chr == ' ' || chr == '\t' || chr == '\r' || chr == '\n' || chr == ';' || chr == ','
				|| chr == '*' || chr == ']';
	}

	private static boolean isNumber(char n) {
		return n >= '0' && n <= '9';
	}

	private static boolean isLetter(char n) {
		return n >= 'A' && n <= 'Z' || n >= 'a' && n <= 'z';
	}

	private String slice() {
		return new String(data, startPos, pos - startPos);
	}

	/**
	 * Lexes an integer type or null if it's not an integer i[0-9]+
	 * 
	 * @return
	 */
	private Token lexInteger() {
		start(0); // Start at the integer parts
		char cur = at();
		while (true) {
			if (!isNumber(cur))
				break;
			cur = next();
		}
		back();
		if (tokenEnd(cur))
			return new TokenInteger(Integer.parseInt(slice()));
		// This isn't an integer (i.e. could be i32foobar)
		rewind();
		return null;
	}

	static private HashMap<String, Keyword> keywords;
	static private HashMap<String, NativeType> nativetypes;
	static private HashMap<String, InstructionTypes> instructions;
	static private HashMap<Character, TokenType> symbols;

	private static boolean isKeywordChar(char x) {
		return isLetter(x) || isNumber(x) || x == '_';
	}

	/**
	 * Lex an identifier. Can be one of four: > Label: [-a-zA-Z$.0-9]+: > Integer:
	 * i[0-9]+ > Keyword: [A-Za-z0-9_]+ (pick from list)
	 * 
	 * @return
	 */
	private Token lexIdentifier(char current) {
		start(-1);
		char x = current;
		do {
			x = next();
		} while (isKeywordChar(x));
		prev();

		// Get the keyword
		String kw = slice();

		// Check if we have a keyword
		if (keywords.containsKey(kw)) {
			return new TokenKeyword(kw, keywords.get(kw));
		}
		// Check if we have a type
		if (nativetypes.containsKey(kw)) {
			return new TokenNativeType(kw, nativetypes.get(kw));
		}
		// Check if we have a type
		if (instructions.containsKey(kw)) {
			return new TokenInstruction(kw, instructions.get(kw));
		}

		throw new Error(String.format("Unknown keyword: \"%s\"\n", kw));
	}

	/**
	 * Lex string
	 * 
	 * Note that we don't try to parse escape sequences here -- that's a job for the
	 * TokenString class
	 * 
	 * @param current
	 * @return
	 */
	private Token lexString(char current) {
		start(-1);
		do {
			current = next();
		} while (current != '"');
		return new TokenString(slice());
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	static private boolean varChar(char x) {
		return x == '-' || x == '.' || (x >= 'A' && x <= 'Z') || (x >= 'a' && x <= 'z') || (x >= '0' && x <= '9')
				|| x == '\\';
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	static private boolean metadataChar(char x) {
		return x == '.' || (x >= 'A' && x <= 'Z') || (x >= 'a' && x <= 'z') || (x >= '0' && x <= '9');
	}

	/**
	 * Lex attribute group: #[0-9]+
	 * 
	 * @return
	 */
	private Token lexAttributeGroup() {
		start(0);
		char current = next();
		while (isNumber(current))
			current = next();
		prev();
		return new TokenAttributeGroup(Integer.parseInt(slice()));
	}

	/**
	 * Parse variable: [%@][-a-zA-Z$._][-a-zA-Z$._0-9]*
	 * 
	 * @param isGlobal
	 * @return
	 */
	private Token parseVar(boolean isGlobal) {
		start(0);
		if (!varChar(next()))
			throw new IllegalStateException("Variable name too short");
		while (varChar(next()))
			;
		prev();
		return isGlobal ? new TokenGlobalVariable(slice()) : new TokenLocalVariable(slice());
	}

	/**
	 * Parse metadata node ![A-Za-z0-9.]
	 * 
	 * @return
	 */
	private Token lexMetadata() {
		start(0);
		char cur = next();
		while (metadataChar(cur))
			cur = next();
		prev();
		if (startPos == pos)
			return new TokenSymbol('!', TokenType.Exclaim); // Just a single '!'
		return new TokenMetadata(slice());
	}

	/**
	 * Parse integer constant [0-9]+
	 * 
	 * @return
	 */
	private Token lexIntegerConstant() {
		start(-1);
		char cur = next();
		while (isNumber(cur))
			cur = next();
		prev();
		return new TokenIntegerConstant(Integer.parseInt(slice()));
	}

	private static Token TokenEOF = new TokenSymbol(' ', TokenType.EOF);

	public Token lex() {
		Token res;
		lastTokenStart = pos;
		out: while (true) {
			char current = next();
			switch (current) {
			case ' ':
			case '\n':
			case '\r':
				continue;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				res = lexIntegerConstant();
				break out;
			case 'i':
				res = lexInteger();
				if (res != null)
					break out;
			case 'A':
			case 'a':
			case 'B':
			case 'b':
			case 'C':
			case 'c':
			case 'D':
			case 'd':
			case 'E':
			case 'e':
			case 'F':
			case 'f':
			case 'G':
			case 'g':
			case 'H':
			case 'h':
			case 'I':
			case 'J':
			case 'j':
			case 'K':
			case 'k':
			case 'L':
			case 'l':
			case 'M':
			case 'm':
			case 'N':
			case 'n':
			case 'O':
			case 'o':
			case 'P':
			case 'p':
			case 'Q':
			case 'q':
			case 'R':
			case 'r':
			case 'S':
			case 's':
			case 'T':
			case 't':
			case 'U':
			case 'u':
			case 'V':
			case 'v':
			case 'W':
			case 'w':
			case 'X':
			case 'x':
			case 'Y':
			case 'y':
			case 'Z':
			case 'z':
			case '_':
				// Parse identifier
				res = lexIdentifier(current);
				break out;
			case ';':
				while (true) {
					current = next();
					if (current == '\n' || current == 0xFFFF)
						break;
				}
				break;
			case '#':
				res = lexAttributeGroup();
				break out;
			case '"':
				res = lexString(current);
				break out;
			case '%':
			case '@':
				res = parseVar(current == '@');
				break out;
			case '!':
				res = lexMetadata();
				break out;
			case 0xFFFF:
				res = TokenEOF;
				break out;
			default:
				if (symbols.containsKey(current)) {
					if (current == '.' && at(0) == '.' && at(1) == '.') {
						res = new TokenSymbol('.', TokenType.DotDotDot);
						skip();
						skip();
					}else
						res = new TokenSymbol(current, symbols.get(current));
					break out;
				}
				throw new IllegalStateException(
						String.format("Unknown character '%c' [%d]", (char) current, (int) current));
			}
		}
		return res;
	}

	public static Lexer loadFromFile(String x) {
		return new Lexer(read(x));
	}

	/**
	 * Read byte array from file
	 * 
	 * https://stackoverflow.com/questions/858980/file-to-byte-in-java
	 * 
	 * @param fn Filename
	 * @return Byte content
	 */
	private static byte[] read(String fn) {
		try {
			File file = new File(fn);
			ByteArrayOutputStream ous = null;
			InputStream ios = null;
			try {
				byte[] buffer = new byte[4096];
				ous = new ByteArrayOutputStream();
				ios = new FileInputStream(file);
				int read = 0;
				while ((read = ios.read(buffer)) != -1) {
					ous.write(buffer, 0, read);
				}
			} finally {
				try {
					if (ous != null)
						ous.close();
				} catch (IOException e) {
				}

				try {
					if (ios != null)
						ios.close();
				} catch (IOException e) {
				}
			}
			return ous.toByteArray();
		} catch (IOException e) {
			System.out.println("Unable to open file");
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	static {
		// Taken straight from the LLVM source code.
		keywords = new HashMap<String, Keyword>();
		nativetypes = new HashMap<String, NativeType>();
		instructions = new HashMap<String, InstructionTypes>();
		symbols = new HashMap<Character, TokenType>();

		keywords.put("true", Keyword.TRUE);
		keywords.put("false", Keyword.FALSE);
		keywords.put("declare", Keyword.DECLARE);
		keywords.put("define", Keyword.DEFINE);
		keywords.put("global", Keyword.GLOBAL);
		keywords.put("constant", Keyword.CONSTANT);
		keywords.put("dso_local", Keyword.DSO_LOCAL);
		keywords.put("dso_preemptable", Keyword.DSO_PREEMPTABLE);
		keywords.put("private", Keyword.PRIVATE);
		keywords.put("internal", Keyword.INTERNAL);
		keywords.put("available_externally", Keyword.AVAILABLE_EXTERNALLY);
		keywords.put("linkonce", Keyword.LINKONCE);
		keywords.put("linkonce_odr", Keyword.LINKONCE_ODR);
		keywords.put("weak", Keyword.WEAK);
		keywords.put("weak_odr", Keyword.WEAK_ODR);
		keywords.put("appending", Keyword.APPENDING);
		keywords.put("dllimport", Keyword.DLLIMPORT);
		keywords.put("dllexport", Keyword.DLLEXPORT);
		keywords.put("common", Keyword.COMMON);
		keywords.put("default", Keyword.DEFAULT);
		keywords.put("hidden", Keyword.HIDDEN);
		keywords.put("protected", Keyword.PROTECTED);
		keywords.put("unnamed_addr", Keyword.UNNAMED_ADDR);
		keywords.put("local_unnamed_addr", Keyword.LOCAL_UNNAMED_ADDR);
		keywords.put("externally_initialized", Keyword.EXTERNALLY_INITIALIZED);
		keywords.put("extern_weak", Keyword.EXTERN_WEAK);
		keywords.put("external", Keyword.EXTERNAL);
		keywords.put("thread_local", Keyword.THREAD_LOCAL);
		keywords.put("localdynamic", Keyword.LOCALDYNAMIC);
		keywords.put("initialexec", Keyword.INITIALEXEC);
		keywords.put("localexec", Keyword.LOCALEXEC);
		keywords.put("zeroinitializer", Keyword.ZEROINITIALIZER);
		keywords.put("undef", Keyword.UNDEF);
		keywords.put("null", Keyword.NULL);
		keywords.put("none", Keyword.NONE);
		keywords.put("to", Keyword.TO);
		keywords.put("caller", Keyword.CALLER);
		keywords.put("within", Keyword.WITHIN);
		keywords.put("from", Keyword.FROM);
		keywords.put("tail", Keyword.TAIL);
		keywords.put("musttail", Keyword.MUSTTAIL);
		keywords.put("notail", Keyword.NOTAIL);
		keywords.put("target", Keyword.TARGET);
		keywords.put("triple", Keyword.TRIPLE);
		keywords.put("source_filename", Keyword.SOURCE_FILENAME);
		keywords.put("unwind", Keyword.UNWIND);
		keywords.put("deplibs", Keyword.DEPLIBS);
		keywords.put("datalayout", Keyword.DATALAYOUT);
		keywords.put("volatile", Keyword.VOLATILE);
		keywords.put("atomic", Keyword.ATOMIC);
		keywords.put("unordered", Keyword.UNORDERED);
		keywords.put("monotonic", Keyword.MONOTONIC);
		keywords.put("acquire", Keyword.ACQUIRE);
		keywords.put("release", Keyword.RELEASE);
		keywords.put("acq_rel", Keyword.ACQ_REL);
		keywords.put("seq_cst", Keyword.SEQ_CST);
		keywords.put("syncscope", Keyword.SYNCSCOPE);
		keywords.put("nnan", Keyword.NNAN);
		keywords.put("ninf", Keyword.NINF);
		keywords.put("nsz", Keyword.NSZ);
		keywords.put("arcp", Keyword.ARCP);
		keywords.put("contract", Keyword.CONTRACT);
		keywords.put("reassoc", Keyword.REASSOC);
		keywords.put("afn", Keyword.AFN);
		keywords.put("fast", Keyword.FAST);
		keywords.put("nuw", Keyword.NUW);
		keywords.put("nsw", Keyword.NSW);
		keywords.put("exact", Keyword.EXACT);
		keywords.put("inbounds", Keyword.INBOUNDS);
		keywords.put("inrange", Keyword.INRANGE);
		keywords.put("align", Keyword.ALIGN);
		keywords.put("addrspace", Keyword.ADDRSPACE);
		keywords.put("section", Keyword.SECTION);
		keywords.put("partition", Keyword.PARTITION);
		keywords.put("alias", Keyword.ALIAS);
		keywords.put("ifunc", Keyword.IFUNC);
		keywords.put("module", Keyword.MODULE);
		keywords.put("asm", Keyword.ASM);
		keywords.put("sideeffect", Keyword.SIDEEFFECT);
		keywords.put("alignstack", Keyword.ALIGNSTACK);
		keywords.put("inteldialect", Keyword.INTELDIALECT);
		keywords.put("gc", Keyword.GC);
		keywords.put("prefix", Keyword.PREFIX);
		keywords.put("prologue", Keyword.PROLOGUE);
		keywords.put("ccc", Keyword.CCC);
		keywords.put("fastcc", Keyword.FASTCC);
		keywords.put("coldcc", Keyword.COLDCC);
		keywords.put("x86_stdcallcc", Keyword.X86_STDCALLCC);
		keywords.put("x86_fastcallcc", Keyword.X86_FASTCALLCC);
		keywords.put("x86_thiscallcc", Keyword.X86_THISCALLCC);
		keywords.put("x86_vectorcallcc", Keyword.X86_VECTORCALLCC);
		keywords.put("arm_apcscc", Keyword.ARM_APCSCC);
		keywords.put("arm_aapcscc", Keyword.ARM_AAPCSCC);
		keywords.put("arm_aapcs_vfpcc", Keyword.ARM_AAPCS_VFPCC);
		keywords.put("aarch64_vector_pcs", Keyword.AARCH64_VECTOR_PCS);
		keywords.put("msp430_intrcc", Keyword.MSP430_INTRCC);
		keywords.put("avr_intrcc", Keyword.AVR_INTRCC);
		keywords.put("avr_signalcc", Keyword.AVR_SIGNALCC);
		keywords.put("ptx_kernel", Keyword.PTX_KERNEL);
		keywords.put("ptx_device", Keyword.PTX_DEVICE);
		keywords.put("spir_kernel", Keyword.SPIR_KERNEL);
		keywords.put("spir_func", Keyword.SPIR_FUNC);
		keywords.put("intel_ocl_bicc", Keyword.INTEL_OCL_BICC);
		keywords.put("x86_64_sysvcc", Keyword.X86_64_SYSVCC);
		keywords.put("win64cc", Keyword.WIN64CC);
		keywords.put("x86_regcallcc", Keyword.X86_REGCALLCC);
		keywords.put("webkit_jscc", Keyword.WEBKIT_JSCC);
		keywords.put("swiftcc", Keyword.SWIFTCC);
		keywords.put("anyregcc", Keyword.ANYREGCC);
		keywords.put("preserve_mostcc", Keyword.PRESERVE_MOSTCC);
		keywords.put("preserve_allcc", Keyword.PRESERVE_ALLCC);
		keywords.put("ghccc", Keyword.GHCCC);
		keywords.put("x86_intrcc", Keyword.X86_INTRCC);
		keywords.put("hhvmcc", Keyword.HHVMCC);
		keywords.put("hhvm_ccc", Keyword.HHVM_CCC);
		keywords.put("cxx_fast_tlscc", Keyword.CXX_FAST_TLSCC);
		keywords.put("amdgpu_vs", Keyword.AMDGPU_VS);
		keywords.put("amdgpu_ls", Keyword.AMDGPU_LS);
		keywords.put("amdgpu_hs", Keyword.AMDGPU_HS);
		keywords.put("amdgpu_es", Keyword.AMDGPU_ES);
		keywords.put("amdgpu_gs", Keyword.AMDGPU_GS);
		keywords.put("amdgpu_ps", Keyword.AMDGPU_PS);
		keywords.put("amdgpu_cs", Keyword.AMDGPU_CS);
		keywords.put("amdgpu_kernel", Keyword.AMDGPU_KERNEL);
		keywords.put("tailcc", Keyword.TAILCC);
		keywords.put("cc", Keyword.CC);
		keywords.put("c", Keyword.C);
		keywords.put("attributes", Keyword.ATTRIBUTES);
		keywords.put("alwaysinline", Keyword.ALWAYSINLINE);
		keywords.put("allocsize", Keyword.ALLOCSIZE);
		keywords.put("argmemonly", Keyword.ARGMEMONLY);
		keywords.put("builtin", Keyword.BUILTIN);
		keywords.put("byval", Keyword.BYVAL);
		keywords.put("inalloca", Keyword.INALLOCA);
		keywords.put("cold", Keyword.COLD);
		keywords.put("convergent", Keyword.CONVERGENT);
		keywords.put("dereferenceable", Keyword.DEREFERENCEABLE);
		keywords.put("dereferenceable_or_null", Keyword.DEREFERENCEABLE_OR_NULL);
		keywords.put("inaccessiblememonly", Keyword.INACCESSIBLEMEMONLY);
		keywords.put("inaccessiblemem_or_argmemonly", Keyword.INACCESSIBLEMEM_OR_ARGMEMONLY);
		keywords.put("inlinehint", Keyword.INLINEHINT);
		keywords.put("inreg", Keyword.INREG);
		keywords.put("jumptable", Keyword.JUMPTABLE);
		keywords.put("minsize", Keyword.MINSIZE);
		keywords.put("naked", Keyword.NAKED);
		keywords.put("nest", Keyword.NEST);
		keywords.put("noalias", Keyword.NOALIAS);
		keywords.put("nobuiltin", Keyword.NOBUILTIN);
		keywords.put("nocapture", Keyword.NOCAPTURE);
		keywords.put("noduplicate", Keyword.NODUPLICATE);
		keywords.put("nofree", Keyword.NOFREE);
		keywords.put("noimplicitfloat", Keyword.NOIMPLICITFLOAT);
		keywords.put("noinline", Keyword.NOINLINE);
		keywords.put("norecurse", Keyword.NORECURSE);
		keywords.put("nonlazybind", Keyword.NONLAZYBIND);
		keywords.put("nonnull", Keyword.NONNULL);
		keywords.put("noredzone", Keyword.NOREDZONE);
		keywords.put("noreturn", Keyword.NORETURN);
		keywords.put("nosync", Keyword.NOSYNC);
		keywords.put("nocf_check", Keyword.NOCF_CHECK);
		keywords.put("nounwind", Keyword.NOUNWIND);
		keywords.put("optforfuzzing", Keyword.OPTFORFUZZING);
		keywords.put("optnone", Keyword.OPTNONE);
		keywords.put("optsize", Keyword.OPTSIZE);
		keywords.put("readnone", Keyword.READNONE);
		keywords.put("readonly", Keyword.READONLY);
		keywords.put("returned", Keyword.RETURNED);
		keywords.put("returns_twice", Keyword.RETURNS_TWICE);
		keywords.put("signext", Keyword.SIGNEXT);
		keywords.put("speculatable", Keyword.SPECULATABLE);
		keywords.put("sret", Keyword.SRET);
		keywords.put("ssp", Keyword.SSP);
		keywords.put("sspreq", Keyword.SSPREQ);
		keywords.put("sspstrong", Keyword.SSPSTRONG);
		keywords.put("strictfp", Keyword.STRICTFP);
		keywords.put("safestack", Keyword.SAFESTACK);
		keywords.put("shadowcallstack", Keyword.SHADOWCALLSTACK);
		keywords.put("sanitize_address", Keyword.SANITIZE_ADDRESS);
		keywords.put("sanitize_hwaddress", Keyword.SANITIZE_HWADDRESS);
		keywords.put("sanitize_memtag", Keyword.SANITIZE_MEMTAG);
		keywords.put("sanitize_thread", Keyword.SANITIZE_THREAD);
		keywords.put("sanitize_memory", Keyword.SANITIZE_MEMORY);
		keywords.put("speculative_load_hardening", Keyword.SPECULATIVE_LOAD_HARDENING);
		keywords.put("swifterror", Keyword.SWIFTERROR);
		keywords.put("swiftself", Keyword.SWIFTSELF);
		keywords.put("uwtable", Keyword.UWTABLE);
		keywords.put("willreturn", Keyword.WILLRETURN);
		keywords.put("writeonly", Keyword.WRITEONLY);
		keywords.put("zeroext", Keyword.ZEROEXT);
		keywords.put("immarg", Keyword.IMMARG);
		keywords.put("type", Keyword.TYPE);
		keywords.put("opaque", Keyword.OPAQUE);
		keywords.put("comdat", Keyword.COMDAT);
		keywords.put("any", Keyword.ANY);
		keywords.put("exactmatch", Keyword.EXACTMATCH);
		keywords.put("largest", Keyword.LARGEST);
		keywords.put("noduplicates", Keyword.NODUPLICATES);
		keywords.put("samesize", Keyword.SAMESIZE);
		keywords.put("eq", Keyword.EQ);
		keywords.put("ne", Keyword.NE);
		keywords.put("slt", Keyword.SLT);
		keywords.put("sgt", Keyword.SGT);
		keywords.put("sle", Keyword.SLE);
		keywords.put("sge", Keyword.SGE);
		keywords.put("ult", Keyword.ULT);
		keywords.put("ugt", Keyword.UGT);
		keywords.put("ule", Keyword.ULE);
		keywords.put("uge", Keyword.UGE);
		keywords.put("oeq", Keyword.OEQ);
		keywords.put("one", Keyword.ONE);
		keywords.put("olt", Keyword.OLT);
		keywords.put("ogt", Keyword.OGT);
		keywords.put("ole", Keyword.OLE);
		keywords.put("oge", Keyword.OGE);
		keywords.put("ord", Keyword.ORD);
		keywords.put("uno", Keyword.UNO);
		keywords.put("ueq", Keyword.UEQ);
		keywords.put("une", Keyword.UNE);
		keywords.put("xchg", Keyword.XCHG);
		keywords.put("nand", Keyword.NAND);
		keywords.put("max", Keyword.MAX);
		keywords.put("min", Keyword.MIN);
		keywords.put("umax", Keyword.UMAX);
		keywords.put("umin", Keyword.UMIN);
		keywords.put("vscale", Keyword.VSCALE);
		keywords.put("x", Keyword.X);
		keywords.put("blockaddress", Keyword.BLOCKADDRESS);
		keywords.put("distinct", Keyword.DISTINCT);
		keywords.put("uselistorder", Keyword.USELISTORDER);
		keywords.put("uselistorder_bb", Keyword.USELISTORDER_BB);
		keywords.put("personality", Keyword.PERSONALITY);
		keywords.put("cleanup", Keyword.CLEANUP);
		keywords.put("catch", Keyword.CATCH);
		keywords.put("filter", Keyword.FILTER);
		keywords.put("path", Keyword.PATH);
		keywords.put("hash", Keyword.HASH);
		keywords.put("gv", Keyword.GV);
		keywords.put("guid", Keyword.GUID);
		keywords.put("name", Keyword.NAME);
		keywords.put("summaries", Keyword.SUMMARIES);
		keywords.put("flags", Keyword.FLAGS);
		keywords.put("linkage", Keyword.LINKAGE);
		keywords.put("notEligibleToImport", Keyword.NOTELIGIBLETOIMPORT);
		keywords.put("live", Keyword.LIVE);
		keywords.put("dsoLocal", Keyword.DSOLOCAL);
		keywords.put("canAutoHide", Keyword.CANAUTOHIDE);
		keywords.put("function", Keyword.FUNCTION);
		keywords.put("insts", Keyword.INSTS);
		keywords.put("funcFlags", Keyword.FUNCFLAGS);
		keywords.put("readNone", Keyword.READNONE);
		keywords.put("readOnly", Keyword.READONLY);
		keywords.put("noRecurse", Keyword.NORECURSE);
		keywords.put("returnDoesNotAlias", Keyword.RETURNDOESNOTALIAS);
		keywords.put("noInline", Keyword.NOINLINE);
		keywords.put("calls", Keyword.CALLS);
		keywords.put("callee", Keyword.CALLEE);
		keywords.put("hotness", Keyword.HOTNESS);
		keywords.put("unknown", Keyword.UNKNOWN);
		keywords.put("hot", Keyword.HOT);
		keywords.put("critical", Keyword.CRITICAL);
		keywords.put("relbf", Keyword.RELBF);
		keywords.put("variable", Keyword.VARIABLE);
		keywords.put("vTableFuncs", Keyword.VTABLEFUNCS);
		keywords.put("virtFunc", Keyword.VIRTFUNC);
		keywords.put("aliasee", Keyword.ALIASEE);
		keywords.put("refs", Keyword.REFS);
		keywords.put("typeIdInfo", Keyword.TYPEIDINFO);
		keywords.put("typeTests", Keyword.TYPETESTS);
		keywords.put("typeTestAssumeVCalls", Keyword.TYPETESTASSUMEVCALLS);
		keywords.put("typeCheckedLoadVCalls", Keyword.TYPECHECKEDLOADVCALLS);
		keywords.put("typeTestAssumeConstVCalls", Keyword.TYPETESTASSUMECONSTVCALLS);
		keywords.put("typeCheckedLoadConstVCalls", Keyword.TYPECHECKEDLOADCONSTVCALLS);
		keywords.put("vFuncId", Keyword.VFUNCID);
		keywords.put("offset", Keyword.OFFSET);
		keywords.put("args", Keyword.ARGS);
		keywords.put("typeid", Keyword.TYPEID);
		keywords.put("typeidCompatibleVTable", Keyword.TYPEIDCOMPATIBLEVTABLE);
		keywords.put("summary", Keyword.SUMMARY);
		keywords.put("typeTestRes", Keyword.TYPETESTRES);
		keywords.put("kind", Keyword.KIND);
		keywords.put("unsat", Keyword.UNSAT);
		keywords.put("byteArray", Keyword.BYTEARRAY);
		keywords.put("inline", Keyword.INLINE);
		keywords.put("single", Keyword.SINGLE);
		keywords.put("allOnes", Keyword.ALLONES);
		keywords.put("sizeM1BitWidth", Keyword.SIZEM1BITWIDTH);
		keywords.put("alignLog2", Keyword.ALIGNLOG2);
		keywords.put("sizeM1", Keyword.SIZEM1);
		keywords.put("bitMask", Keyword.BITMASK);
		keywords.put("inlineBits", Keyword.INLINEBITS);
		keywords.put("wpdResolutions", Keyword.WPDRESOLUTIONS);
		keywords.put("wpdRes", Keyword.WPDRES);
		keywords.put("indir", Keyword.INDIR);
		keywords.put("singleImpl", Keyword.SINGLEIMPL);
		keywords.put("branchFunnel", Keyword.BRANCHFUNNEL);
		keywords.put("singleImplName", Keyword.SINGLEIMPLNAME);
		keywords.put("resByArg", Keyword.RESBYARG);
		keywords.put("byArg", Keyword.BYARG);
		keywords.put("uniformRetVal", Keyword.UNIFORMRETVAL);
		keywords.put("uniqueRetVal", Keyword.UNIQUERETVAL);
		keywords.put("virtualConstProp", Keyword.VIRTUALCONSTPROP);
		keywords.put("info", Keyword.INFO);
		keywords.put("byte", Keyword.BYTE);
		keywords.put("bit", Keyword.BIT);
		keywords.put("varFlags", Keyword.VARFLAGS);

		nativetypes.put("void", NativeType.VOID);
		nativetypes.put("half", NativeType.HALF);
		nativetypes.put("float", NativeType.FLOAT);
		nativetypes.put("double", NativeType.DOUBLE);
		nativetypes.put("x86_fp80", NativeType.X86_FP80);
		nativetypes.put("fp128", NativeType.FP128);
		nativetypes.put("ppc_fp128", NativeType.PPC_FP128);
		nativetypes.put("label", NativeType.LABEL);
		nativetypes.put("metadata", NativeType.METADATA);
		nativetypes.put("x86_mmx", NativeType.X86_MMX);
		nativetypes.put("token", NativeType.TOKEN);

		instructions.put("fneg", InstructionTypes.FNEG);
		instructions.put("add", InstructionTypes.ADD);
		instructions.put("fadd", InstructionTypes.FADD);
		instructions.put("sub", InstructionTypes.SUB);
		instructions.put("fsub", InstructionTypes.FSUB);
		instructions.put("mul", InstructionTypes.MUL);
		instructions.put("fmul", InstructionTypes.FMUL);
		instructions.put("udiv", InstructionTypes.UDIV);
		instructions.put("sdiv", InstructionTypes.SDIV);
		instructions.put("fdiv", InstructionTypes.FDIV);
		instructions.put("urem", InstructionTypes.UREM);
		instructions.put("srem", InstructionTypes.SREM);
		instructions.put("frem", InstructionTypes.FREM);
		instructions.put("shl", InstructionTypes.SHL);
		instructions.put("lshr", InstructionTypes.LSHR);
		instructions.put("ashr", InstructionTypes.ASHR);
		instructions.put("and", InstructionTypes.AND);
		instructions.put("or", InstructionTypes.OR);
		instructions.put("xor", InstructionTypes.XOR);
		instructions.put("icmp", InstructionTypes.ICMP);
		instructions.put("fcmp", InstructionTypes.FCMP);
		instructions.put("phi", InstructionTypes.PHI);
		instructions.put("call", InstructionTypes.CALL);
		instructions.put("trunc", InstructionTypes.TRUNC);
		instructions.put("zext", InstructionTypes.ZEXT);
		instructions.put("sext", InstructionTypes.SEXT);
		instructions.put("fptrunc", InstructionTypes.FPTRUNC);
		instructions.put("fpext", InstructionTypes.FPEXT);
		instructions.put("uitofp", InstructionTypes.UITOFP);
		instructions.put("sitofp", InstructionTypes.SITOFP);
		instructions.put("fptoui", InstructionTypes.FPTOUI);
		instructions.put("fptosi", InstructionTypes.FPTOSI);
		instructions.put("inttoptr", InstructionTypes.INTTOPTR);
		instructions.put("ptrtoint", InstructionTypes.PTRTOINT);
		instructions.put("bitcast", InstructionTypes.BITCAST);
		instructions.put("addrspacecast", InstructionTypes.ADDRSPACECAST);
		instructions.put("select", InstructionTypes.SELECT);
		instructions.put("va_arg", InstructionTypes.VA_ARG);
		instructions.put("ret", InstructionTypes.RET);
		instructions.put("br", InstructionTypes.BR);
		instructions.put("switch", InstructionTypes.SWITCH);
		instructions.put("indirectbr", InstructionTypes.INDIRECTBR);
		instructions.put("invoke", InstructionTypes.INVOKE);
		instructions.put("resume", InstructionTypes.RESUME);
		instructions.put("unreachable", InstructionTypes.UNREACHABLE);
		instructions.put("callbr", InstructionTypes.CALLBR);
		instructions.put("alloca", InstructionTypes.ALLOCA);
		instructions.put("load", InstructionTypes.LOAD);
		instructions.put("store", InstructionTypes.STORE);
		instructions.put("cmpxchg", InstructionTypes.CMPXCHG);
		instructions.put("atomicrmw", InstructionTypes.ATOMICRMW);
		instructions.put("fence", InstructionTypes.FENCE);
		instructions.put("getelementptr", InstructionTypes.GETELEMENTPTR);
		instructions.put("extractelement", InstructionTypes.EXTRACTELEMENT);
		instructions.put("insertelement", InstructionTypes.INSERTELEMENT);
		instructions.put("shufflevector", InstructionTypes.SHUFFLEVECTOR);
		instructions.put("extractvalue", InstructionTypes.EXTRACTVALUE);
		instructions.put("insertvalue", InstructionTypes.INSERTVALUE);
		instructions.put("landingpad", InstructionTypes.LANDINGPAD);
		instructions.put("cleanupret", InstructionTypes.CLEANUPRET);
		instructions.put("catchret", InstructionTypes.CATCHRET);
		instructions.put("catchswitch", InstructionTypes.CATCHSWITCH);
		instructions.put("catchpad", InstructionTypes.CATCHPAD);
		instructions.put("cleanuppad", InstructionTypes.CLEANUPPAD);

		symbols.put('.', TokenType.Dot);
		symbols.put(',', TokenType.Comma);
		symbols.put('=', TokenType.Equal);
		symbols.put('(', TokenType.LParen);
		symbols.put(')', TokenType.RParen);
		symbols.put('{', TokenType.LBrace);
		symbols.put('}', TokenType.RBrace);
		symbols.put('*', TokenType.Star);
		symbols.put('#', TokenType.Hash);
		symbols.put('[', TokenType.LBracket);
		symbols.put(']', TokenType.RBracket);
	};
}

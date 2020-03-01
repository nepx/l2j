package l2j.parser;

import java.util.ArrayList;
import java.util.HashMap;

import l2j.lexer.*;
import l2j.lexer.token.*;
import l2j.module.GlobalVariable;
import l2j.module.Linkage;
import l2j.module.Module;
import l2j.module.attributes.*;
import l2j.module.function.*;
import l2j.module.function.instruction.*;
import l2j.module.types.*;
import l2j.module.value.*;
import l2j.module.value.ValueGetElementPtr.TypeValuePair;

/**
 * LLVM parser
 * 
 * @see http://llvm.org/doxygen/LLParser_8cpp_source.html
 * @see https://llvm.org/docs/LangRef.html
 * @author jkim13
 *
 */
public class Parser {
	private Lexer l;

	public Parser(Lexer l) {
		this.l = l;
	}

	private static void keyword(Token t) {
		if (t.type != TokenType.Keyword)
			throw new IllegalStateException("Expected keyword, got " + t.type.toString());
	}

	private static void mustBe(Token t, TokenType k) {
		if (t.type != k)
			throw new IllegalStateException("Expected " + k.toString() + ", got " + t.type.toString());
	}

	private static void mustBe(Token t, Keyword k) {
		if (t.type != TokenType.Keyword)
			throw new IllegalStateException("Expected Keyword, got " + t.type.toString());
		TokenKeyword kw = (TokenKeyword) t;
		if (kw.kwe != k)
			throw new IllegalStateException("Expected " + kw.kwe.toString() + ", got " + t.type.toString());
	}

	private static boolean is(Token t, Keyword k) {
		if (t.type != TokenType.Keyword)
			return false;
		return ((TokenKeyword) t).kwe == k;
	}

	private static boolean is(Token t, TokenType tt) {
		return t.type == tt;
	}

	private static int getInteger(Token t) {
		if (t.type != TokenType.IntegerConstant)
			throw new IllegalStateException("Expected integer constant");
		return ((TokenIntegerConstant) t).value;
	}

	private static String getString(Token t) {
		if (t.type != TokenType.String)
			throw new IllegalStateException("Expected string constant");
		return ((TokenString) t).data;
	}

	/**
	 * Parse linkage type.
	 * 
	 * @param t           Token to be examined
	 * @param visibillity Visibility statement to be modified
	 * @return Next token
	 */
	private Token parseOptionalLinkageAux(Token t, Linkage visibillity) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case PRIVATE:
			visibillity.value = Linkage.VISIBILITY_PRIVATE;
			break;
		case INTERNAL:
			visibillity.value = Linkage.VISIBILITY_INTERNAL;
			break;
		case AVAILABLE_EXTERNALLY:
			visibillity.value = Linkage.VISIBILITY_EXTERNAL;
			break;
		case LINKONCE:
		case WEAK:
		case COMMON:
		case APPENDING:
		case EXTERN_WEAK:
		case LINKONCE_ODR:
		case WEAK_ODR:
		case EXTERNAL:
			visibillity.value = Linkage.VISIBILITY_DEFAULT;
			break;
		default:
			visibillity.value = Linkage.VISIBILITY_DEFAULT;
			return t;
		}
		return l.lex();
	}

	/**
	 * Parse DSO local.
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Token parseOptionalDSO(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case DSO_LOCAL:
		case DSO_PREEMPTABLE:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse visibility.
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Token parseOptionalVisibility(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case DEFAULT:
		case HIDDEN:
		case PROTECTED:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse DLL storage.
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Token parseOptionalDLLStorage(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case DLLIMPORT:
		case DLLEXPORT:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse thread local storage model https://llvm.org/docs/LangRef.html#tls-model
	 * 
	 * @param t
	 * @return
	 */
	private Token parseTLSModel(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case LOCALDYNAMIC:
		case INITIALEXEC:
		case LOCALEXEC:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse optional linkage
	 * 
	 * @param kw
	 * @param l
	 * @return
	 */
	private Token parseOptionalLinkage(Token t, Linkage l) {
		t = parseOptionalLinkageAux(t, l);
		t = parseOptionalDSO(t);
		t = parseOptionalVisibility(t);
		t = parseOptionalDLLStorage(t);
		return t;
	}

	/**
	 * Parse optional calling convention
	 * 
	 * @param t
	 * @return
	 */
	private Token parseOptionalCallingConvention(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case CC:
			mustBe(l.lex(), TokenType.IntegerConstant);
			// INTENTIONAL FALLTHROUGH
		case FASTCC:
		case INTEL_OCL_BICC:
		case COLDCC:
		case X86_STDCALLCC:
		case X86_FASTCALLCC:
		case X86_THISCALLCC:
		case X86_VECTORCALLCC:
		case ARM_APCSCC:
		case ARM_AAPCSCC:
		case ARM_AAPCS_VFPCC:
		case AARCH64_VECTOR_PCS:
		case MSP430_INTRCC:
		case AVR_INTRCC:
		case AVR_SIGNALCC:
		case PTX_KERNEL:
		case PTX_DEVICE:
		case SPIR_FUNC:
		case SPIR_KERNEL:
		case X86_64_SYSVCC:
		case WIN64CC:
		case WEBKIT_JSCC:
		case ANYREGCC:
		case PRESERVE_MOSTCC:
		case PRESERVE_ALLCC:
		case GHCCC:
		case SWIFTCC:
		case X86_INTRCC:
		case HHVMCC:
		case HHVM_CCC:
		case CXX_FAST_TLSCC:
		case AMDGPU_VS:
		case AMDGPU_LS:
		case AMDGPU_HS:
		case AMDGPU_ES:
		case AMDGPU_GS:
		case AMDGPU_PS:
		case AMDGPU_CS:
		case AMDGPU_KERNEL:
		case TAILCC:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse string attribute
	 * 
	 * @param s
	 * @param add The list to add the parsed attribute
	 * @return
	 */
	private Token parseStringAttribute(TokenString s, ArrayList<Attribute> add) {
		throw new Error("TODO: String attributes");
	}

	/**
	 * Parse alignment
	 * 
	 * @param add
	 */
	private void parseAlignment(ArrayList<Attribute> add) {
		Token t = l.lex();
		mustBe(t, TokenType.IntegerConstant);
		add.add(new AttributeAlign(((TokenIntegerConstant) t).value));
	}

	/**
	 * Parse type
	 * 
	 * @param t
	 * @return
	 */
	private Type parseType(Token t) {
		if (t == null)
			t = l.lex();
		Type result = null;
		switch (t.type) {
		case LBracket: {
			// [32 x i8]
			int count = getInteger(l.lex());
			mustBe(l.lex(), Keyword.X);
			Type type = parseType(null);
			mustBe(l.lex(), TokenType.RBracket);
			result = new ArrayType(type, count);
			break;
		}
		case Integer:
			result = new IntegerType(((TokenInteger) t).width);
			break;
		default:
			break;
		}
		if (result == null)
			return null;
		t = l.lex();
		while (t.type == TokenType.Star) {
			result = new PointerType(result);
			t = l.lex();
		}
		if (t.type == TokenType.LParen) {
			ArrayList<Type> params = new ArrayList<Type>();
			t = l.lex();
			while (t.type != TokenType.RParen) {
				params.add(parseType(t));
				t = l.lex();
				if (t.type == TokenType.Comma)
					t = l.lex();
			}
			l.lex();
		}
		l.unlex();
		return result;
	}

	private ArrayList<GlobalValueVector> parseGlobalValueVector(Token t) {
		if (t.type == TokenType.RBrace || t.type == TokenType.RBracket)
			return null;
		ArrayList<GlobalValueVector> list = new ArrayList<GlobalValueVector>();
		while (true) {
			boolean inrange = is(t, Keyword.INRANGE);
			if (inrange)
				t = l.lex();
			Type type = parseType(t);
			t = l.lex();
			Value val = parseValue(t, null);
			t = l.lex();
			list.add(new GlobalValueVector(type, val, inrange));
			if (t.type != TokenType.Comma) {
				l.unlex();
				return list;
			}
			t = l.lex(); // Skip comma and move to the next keyword, which will be used at top of loop
		}
	}

	/**
	 * Parse a value, either a constant or a local variable
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Value parseValue(Token t, Function f) {
		boolean undo = false;
		if (t == null) {
			undo = true;
			t = l.lex();
		}
		switch (t.type) {
		case IntegerConstant:
			return new ValueConstant(getInteger(t));
		case GlobalVariable:
			return new ValueGlobalVariable(((TokenGlobalVariable) t).name);
		case LocalVariable:
			if (f == null)
				throw new IllegalStateException("what's a local variable doing here??");
			return new ValueLocalVariable(((TokenLocalVariable) t).name, f);
		case Instruction: {
			// "getelementptr"
			InstructionTypes kwe = ((TokenInstruction) t).kwe;
			switch (kwe) {
			case GETELEMENTPTR: {
				t = l.lex();
				boolean inbounds = is(t, Keyword.INBOUNDS);
				if (inbounds)
					t = l.lex();
				mustBe(t, TokenType.LParen);
				Type typ = parseType(l.lex());
				mustBe(l.lex(), TokenType.Comma);
				t = l.lex();
				ArrayList<GlobalValueVector> o = parseGlobalValueVector(t);
				if (o != null)
					t = l.lex();
				return new ValueGetElementPtr(inbounds, typ, o);
			}
			}
			throw new UnsupportedOperationException("unknown value keyword: " + kwe.toString());
		}
		case Keyword: {
			Keyword kwe = ((TokenKeyword) t).kwe;
			switch (kwe) {
			case C: {
				// c"Hello, world!"
				t = l.lex();
				mustBe(t, TokenType.String);
				return new ValueString((TokenString) t);
			}
			default:
				break; // Don't do anything -- just quit
			}
			// FALLTHROUGH
		}
		default:
			if (undo)
				l.unlex();
			return null;
		}
	}

	/**
	 * Parse byval
	 * 
	 * @param add
	 */
	private void parseByval(ArrayList<Attribute> add) {
		Token t = l.lex();
		if (t.type == TokenType.LParen) {
			add.add(new AttributeByval(parseType(l.lex())));
		} else {
			add.add(new AttributeByval(null));
		}
	}

	private static HashMap<Keyword, AttributeType> attrsTable = new HashMap<Keyword, AttributeType>();
	static {
		attrsTable.put(Keyword.UWTABLE, AttributeType.Uwtable);
		attrsTable.put(Keyword.NOUNWIND, AttributeType.Nounwind);
	};

	/**
	 * Parse optional function attributes
	 * 
	 * @param t
	 * @param f
	 * @param ag Whether this is an attribute group listing or not
	 * @return
	 */
	private Token parseOptionalFunctionAttributes(Token t, ArrayList<Attribute> attributess, boolean ag) {
		while (true) {
			switch (t.type) {
			case AttributeGroup:
				attributess.add(new AttributeGroup(((TokenAttributeGroup) t).id));
				break;
			case String: {
				Token orig = t;
				t = l.lex();
				if (t.type == TokenType.Equal) {
					t = l.lex();
					mustBe(t, TokenType.String);
					attributess.add(new AttributeTargetSpecific(getString(orig), getString(t)));
				} else {
					attributess.add(new AttributeTargetSpecific(getString(t), null));
					l.unlex();
				}
				break;
			}
			case Keyword: {
				TokenKeyword kw = (TokenKeyword) t;
				t = l.lex();
				switch (kw.kwe) {
				case ALIGN:
					if (ag) {
						mustBe(t, TokenType.Equal);
						t = l.lex();
					}
					attributess.add(new AttributeAlign(getInteger(t)));
					break;
				default:
					if (attrsTable.containsKey(kw.kwe)) {
						attributess.add(new AttributeGeneric(attrsTable.get(kw.kwe)));
						break;
					}
					throw new UnsupportedOperationException("TODO: Attr " + kw.kwe.toString());
				}
				break;
			}
			default:
				return t;
			}
			t = l.lex();
		}
	}

	private Token parseOptionalFunctionAttributes(Token t, Callable f) {
		return parseOptionalFunctionAttributes(t, f.attributess, false);
	}

	private Token parseOptionalFunctionAttributes(Token t, AttributeList f) {
		return parseOptionalFunctionAttributes(t, f.arr, true);
	}

	/**
	 * Parse optional return attributes
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Token parseOptionalReturnAttributes(Token t, ArrayList<Attribute> f) {
		out: while (true) {
			if (t.type == TokenType.String)
				t = parseStringAttribute((TokenString) t, f);
			else {
				if (t.type != TokenType.Keyword)
					break;
				switch (((TokenKeyword) t).kwe) {
				default:
					break out;
				case ALIGN:
					parseAlignment(f);
					break;
				case BYVAL:
					parseByval(f);
					break;
				// TODO: Add more
				}
			}
			t = l.lex();
		}
		return t;
	}

	/**
	 * Parse fast math flags
	 * 
	 * @param t
	 * @return
	 */
	private Token parseFastMathFlags(Token t) {
		if (t == null)
			t = l.lex();
		out: while (true) {
			if (t.type != TokenType.Keyword)
				break;
			Keyword kwe = ((TokenKeyword) t).kwe;
			switch (kwe) {
			case NNAN:
			case NINF:
			case NSZ:
			case ARCP:
			case CONTRACT:
			case AFN:
			case REASSOC:
			case FAST:
				t = l.lex();
				continue;
			default:
				break out;
			}
		}
		return t;
	}

	/**
	 * Parse unnamed_addr attribute. Currently does nothing.
	 * 
	 * @param t
	 * @return
	 */
	private Token parseOptionalUnnamedAddr(Token t) {
		if (t.type != TokenType.Keyword)
			return t;
		switch (((TokenKeyword) t).kwe) {
		case UNNAMED_ADDR:
		case LOCAL_UNNAMED_ADDR:
			return l.lex();
		default:
			return t;
		}
	}

	/**
	 * Parse instruction
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Instruction parseInstruction(Token t, Function f) {
		// Check if it's an assignment
		String destination = null;
		Variable dest = null;
		if (t.type == TokenType.LocalVariable) {
			destination = ((TokenLocalVariable) t).name;
			mustBe(l.lex(), TokenType.Equal);
			t = l.lex();
			dest = new LocalVariable(destination, f);
			f.lvars.put(destination, (LocalVariable) dest);
		}
		mustBe(t, TokenType.Instruction);
		Instruction insn = null;
		switch (((TokenInstruction) t).kwe) {
		case RET: {
			Type type = parseType(null);
			Value value = parseValue(null, f);

			insn = new InstructionRet(type, value);
			break;
		}
		case STORE: {
			t = l.lex();
			boolean atomic = is(t, Keyword.ATOMIC);
			if (atomic)
				t = l.lex();
			boolean isVolatile = is(t, Keyword.VOLATILE);
			if (isVolatile)
				t = l.lex();
			Type type = parseType(t);
			Value value = parseValue(null, f);
			mustBe(l.lex(), TokenType.Comma);
			Type pointerType = parseType(null); // Note: parseType eats pointer asterixes
			Value pointer = parseValue(null, f);
			t = l.lex();

			int align = 0, nontemporal = 0, invariant = 0;
			while (t.type == TokenType.Comma) {
				t = l.lex();
				if (is(t, Keyword.ALIGN)) {
					align = getInteger(l.lex());
					t = l.lex();
					continue;
				}
			}
			l.unlex();
			insn = new InstructionStore(atomic, isVolatile, type, value, pointerType, pointer, align, nontemporal,
					invariant);
			break;
		}
		case ALLOCA: {
			t = l.lex();
			boolean inalloca = is(t, Keyword.INALLOCA);
			if (inalloca)
				t = l.lex();

			Type type = parseType(t), numElementsType = null;
			int numElements = 0, align = 0, addrspace = 0;

			if (type == null)
				throw new IllegalStateException("Expected type after alloca");

			t = l.lex();
			while (t.type == TokenType.Comma) {
				// 1. <type> <NumberElements>
				// 2. align <number>
				// 3. addrspace(<number>)
				t = l.lex();
				if ((numElementsType = parseType(t)) != null) {
					// Option 1
					numElements = getInteger(l.lex());
					t = l.lex();
					continue;
				} else {
					if (is(t, Keyword.ALIGN)) {
						align = getInteger(l.lex());
						t = l.lex();
						continue;
					} else if (is(t, Keyword.ADDRSPACE)) {
						mustBe(l.lex(), TokenType.LParen);
						addrspace = getInteger(l.lex());
						mustBe(l.lex(), TokenType.RParen);
						t = l.lex();
						continue;
					}
				}
				break;
			}
			l.unlex();
			insn = new InstructionAlloca(inalloca, type, numElementsType, numElements, align, addrspace);
			break;
		}
		case CALL: {
			t = l.lex();
			t = parseFastMathFlags(t);
			t = parseOptionalCallingConvention(t);
			Type returnType = parseType(t);
			if (returnType == null)
				throw new IllegalStateException("Expected type");

			Value fnptrval = parseValue(null, f);
			ArrayList<Value> args = new ArrayList<Value>();
			mustBe(l.lex(), TokenType.LParen);
			t = l.lex();
			while (t.type != TokenType.RParen) {
				System.out.println("type: " + t);
				Type paramtype = parseType(t);
				t = l.lex();
				System.out.println("val: " + t);
				Value x = parseValue(t, f);
				if (x == null)
					throw new IllegalStateException("Expected parameter inside argument list");
				args.add(x);
				t = l.lex();
			}
			insn = new InstructionCall(returnType, fnptrval, args);
			break;
		}
		default:
			throw new UnsupportedOperationException("Unknown instruction: " + ((TokenInstruction) t).kwe);
		}
		insn.destination = dest;
		return insn;
	}

	/**
	 * Parse the function body
	 * 
	 * @param t
	 * @param f
	 * @return
	 */
	private Token parseFunctionBody(Token t, Function f) {
		BasicBlock b = new BasicBlock();
		while (t.type != TokenType.RBrace) {
			String name;
			Instruction i;
			if (t.type == TokenType.Label) {
				throw new UnsupportedOperationException("todo: label");
			} else {
				name = f.getTempName();
			}
			do {
				i = parseInstruction(t, f);
				b.instructions.add(i);
				t = l.lex();
			} while (!i.isTerminator());
			f.blocks.put(name, b);
		}
		return t;
	}

	/**
	 * Parse a function header, as described by the LLVM specification
	 * 
	 * @param t                 Token
	 * @param f                 Function
	 * @param parseFunctionBody self-explanatory
	 * @return Next token in stream
	 */
	private Token parseFunctionHeader(Token t, Callable f, boolean parseFunctionBody) {
		t = parseOptionalLinkage(t, f.visibillity);
		t = parseOptionalCallingConvention(t);
		t = parseOptionalReturnAttributes(t, f.returnAttributes);

		f.returnType = parseType(t);
		t = l.lex();
		if (t.type != TokenType.GlobalVariable)
			throw new IllegalStateException("define name must be global variable");

		f.name = ((TokenGlobalVariable) t).name;

		mustBe(l.lex(), TokenType.LParen);

		t = l.lex();
		while (t.type != TokenType.RParen) {
			Parameter param = new Parameter();
			param.type = parseType(t);

			t = l.lex();
			// TODO: Parse attributes

			t = l.lex();
			if (t.type == TokenType.LocalVariable) {
				param.name = ((TokenLocalVariable) t).name;
			} else {
				if (t.type == TokenType.Comma)
					t = l.lex();
				else if (t.type == TokenType.Keyword)
					throw new IllegalStateException("TODO: Parse attributes");
			}
		}

		mustBe(t, TokenType.RParen);
		t = l.lex();

		t = parseOptionalUnnamedAddr(t);
		t = parseOptionalFunctionAttributes(t, f);
		mustBe(t, TokenType.LBrace);

		if (parseFunctionBody)
			parseFunctionBody(l.lex(), (Function) f);

		return t;
	}

	public void parse(Module m) {
		out: while (true) {
			Token t = l.lex();
			switch (t.type) {
			case EOF:
				break out;
			case Metadata:
				mustBe(l.lex(), TokenType.Equal);
				mustBe(l.lex(), TokenType.Exclaim);
				mustBe(l.lex(), TokenType.LBrace);
				t = l.lex();

				// TODO: read metadata
				while (t.type != TokenType.RBrace)
					t = l.lex();
				break;
			case GlobalVariable: {
				mustBe(l.lex(), TokenType.Equal);
				t = l.lex();
				GlobalVariable g = new GlobalVariable();
				t = parseOptionalLinkage(t, g.visibility);
				t = parseTLSModel(t);
				t = parseOptionalUnnamedAddr(t);
				mustBe(t, TokenType.Keyword);

				boolean constant = is(t, Keyword.CONSTANT);
				// if(!constant) global = true
				Type typ = parseType(null);
				Value v = parseValue(null, null);
				int align = 1;

				// Parse ending attributes
				t = l.lex();
				while (t.type == TokenType.Comma) {
					t = l.lex();
					if (t.type == TokenType.Metadata)
						throw new UnsupportedOperationException("TODO: metadata");
					if (t.type == TokenType.Keyword) {
						switch (((TokenKeyword) t).kwe) {
						case SECTION:
							mustBe(l.lex(), TokenType.String);
							break;
						case COMDAT:
							throw new UnsupportedOperationException("TODO: comdat");
						case ALIGN:
							align = getInteger(l.lex());
							break;
						default:
							throw new IllegalStateException("Illegal keyword after global variable definition");
						}
					} else // Just skip the token, I guess
						break;
					t = l.lex();
				}
				l.unlex();
				break;
			}
			case Keyword: {
				TokenKeyword kw = (TokenKeyword) t;
				switch (kw.kwe) {
				case TARGET:
					/// target [triple|datalayout] = "[string]"
					t = l.lex();
					keyword(t);
					kw = (TokenKeyword) t;
					if (kw.kwe != Keyword.TRIPLE && kw.kwe != Keyword.DATALAYOUT)
						throw new IllegalStateException();

					// Must be an equal sign
					mustBe(l.lex(), TokenType.Equal);

					// Now must be a string
					t = l.lex();
					mustBe(t, TokenType.String);

					if (kw.kwe == Keyword.TRIPLE)
						m.targetTriple = t.toString();
					else
						m.datalayout = t.toString();
					break;
				case DEFINE: {
					Function f = new Function();
					t = l.lex();

					parseFunctionHeader(t, f, true);

					// Add the function to our list.
					m.functions.add(f);
					break;
				}
				case DECLARE: {
					ExternalFunction f = new ExternalFunction();
					t = l.lex();

					parseFunctionHeader(t, f, false);

					// Add the function to our list.
					m.functions.add(f);
				}
				case ATTRIBUTES: {
					Token temp;
					mustBe(temp = l.lex(), TokenType.AttributeGroup);
					mustBe(l.lex(), TokenType.Equal);
					mustBe(l.lex(), TokenType.LBrace);
					t = l.lex();
					AttributeList attrs = new AttributeList();
					while (t.type != TokenType.RBrace)
						t = parseOptionalFunctionAttributes(t, attrs);
					m.attributes.put(((TokenAttributeGroup) temp).id, attrs);
					break;
				}
				case SOURCE_FILENAME: // i dont care
					mustBe(l.lex(), TokenType.Equal);
					mustBe(l.lex(), TokenType.String);
					break;
				default:
					throw new IllegalStateException("Unknown keyword " + kw.kw);
				}
			}
			}
		}
	}
}

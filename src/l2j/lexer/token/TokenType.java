package l2j.lexer.token;

public enum TokenType {
	EOF,
	Integer,
	Keyword,
	NativeType,
	Instruction,
	String,
	IntegerConstant,
	GlobalVariable,
	LocalVariable,
	AttributeGroup,
	Metadata,
	
	Label,
	
	Comma,
	Equal,
	LParen,
	RParen,
	LBrace,
	RBrace,
	Star,
	Hash,
	Exclaim,
	LBracket,
	RBracket,
	Dot,
	DotDotDot // "..."
}

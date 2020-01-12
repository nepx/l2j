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
	
	Label, // TODO
	
	Comma,
	Equal,
	LParen,
	RParen,
	LBrace,
	RBrace,
	Star,
	Hash
}

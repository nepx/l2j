package l2j.lexer.token;

public enum Keyword {
	TRUE, FALSE, DECLARE, DEFINE, GLOBAL, CONSTANT, DSO_LOCAL, DSO_PREEMPTABLE, PRIVATE, INTERNAL, AVAILABLE_EXTERNALLY,
	LINKONCE, LINKONCE_ODR, WEAK, WEAK_ODR, APPENDING, DLLIMPORT, DLLEXPORT, COMMON, DEFAULT, HIDDEN, PROTECTED,
	UNNAMED_ADDR, LOCAL_UNNAMED_ADDR, EXTERNALLY_INITIALIZED, EXTERN_WEAK, EXTERNAL, THREAD_LOCAL, LOCALDYNAMIC,
	INITIALEXEC, LOCALEXEC, ZEROINITIALIZER, UNDEF, NULL, NONE, TO, CALLER, WITHIN, FROM, TAIL, MUSTTAIL, NOTAIL,
	TARGET, TRIPLE, SOURCE_FILENAME, UNWIND, DEPLIBS, DATALAYOUT, VOLATILE, ATOMIC, UNORDERED, MONOTONIC, ACQUIRE,
	RELEASE, ACQ_REL, SEQ_CST, SYNCSCOPE, NNAN, NINF, NSZ, ARCP, CONTRACT, REASSOC, AFN, FAST, NUW, NSW, EXACT,
	INBOUNDS, INRANGE, ALIGN, ADDRSPACE, SECTION, PARTITION, ALIAS, IFUNC, MODULE, ASM, SIDEEFFECT, ALIGNSTACK,
	INTELDIALECT, GC, PREFIX, PROLOGUE, CCC, FASTCC, COLDCC, X86_STDCALLCC, X86_FASTCALLCC, X86_THISCALLCC,
	X86_VECTORCALLCC, ARM_APCSCC, ARM_AAPCSCC, ARM_AAPCS_VFPCC, AARCH64_VECTOR_PCS, MSP430_INTRCC, AVR_INTRCC,
	AVR_SIGNALCC, PTX_KERNEL, PTX_DEVICE, SPIR_KERNEL, SPIR_FUNC, INTEL_OCL_BICC, X86_64_SYSVCC, WIN64CC, X86_REGCALLCC,
	WEBKIT_JSCC, SWIFTCC, ANYREGCC, PRESERVE_MOSTCC, PRESERVE_ALLCC, GHCCC, X86_INTRCC, HHVMCC, HHVM_CCC,
	CXX_FAST_TLSCC, AMDGPU_VS, AMDGPU_LS, AMDGPU_HS, AMDGPU_ES, AMDGPU_GS, AMDGPU_PS, AMDGPU_CS, AMDGPU_KERNEL, TAILCC,
	CC, C, ATTRIBUTES, ALWAYSINLINE, ALLOCSIZE, ARGMEMONLY, BUILTIN, BYVAL, INALLOCA, COLD, CONVERGENT, DEREFERENCEABLE,
	DEREFERENCEABLE_OR_NULL, INACCESSIBLEMEMONLY, INACCESSIBLEMEM_OR_ARGMEMONLY, INLINEHINT, INREG, JUMPTABLE, MINSIZE,
	NAKED, NEST, NOALIAS, NOBUILTIN, NOCAPTURE, NODUPLICATE, NOFREE, NOIMPLICITFLOAT, NORECURSE, NONLAZYBIND,
	NONNULL, NOREDZONE, NORETURN, NOSYNC, NOCF_CHECK, NOUNWIND, OPTFORFUZZING, OPTNONE, OPTSIZE, READNONE, READONLY,
	RETURNED, RETURNS_TWICE, SIGNEXT, SPECULATABLE, SRET, SSP, SSPREQ, SSPSTRONG, STRICTFP, SAFESTACK, SHADOWCALLSTACK,
	SANITIZE_ADDRESS, SANITIZE_HWADDRESS, SANITIZE_MEMTAG, SANITIZE_THREAD, SANITIZE_MEMORY, SPECULATIVE_LOAD_HARDENING,
	SWIFTERROR, SWIFTSELF, UWTABLE, WILLRETURN, WRITEONLY, ZEROEXT, IMMARG, TYPE, OPAQUE, COMDAT, ANY, EXACTMATCH,
	LARGEST, NODUPLICATES, SAMESIZE, EQ, NE, SLT, SGT, SLE, SGE, ULT, UGT, ULE, UGE, OEQ, ONE, OLT, OGT, OLE, OGE, ORD,
	UNO, UEQ, UNE, XCHG, NAND, MAX, MIN, UMAX, UMIN, VSCALE, X, BLOCKADDRESS, DISTINCT, USELISTORDER, USELISTORDER_BB,
	PERSONALITY, CLEANUP, CATCH, FILTER, PATH, HASH, GV, GUID, NAME, SUMMARIES, FLAGS, LINKAGE, NOTELIGIBLETOIMPORT,
	LIVE, DSOLOCAL, CANAUTOHIDE, FUNCTION, INSTS, FUNCFLAGS, RETURNDOESNOTALIAS,
	NOINLINE, CALLS, CALLEE, HOTNESS, UNKNOWN, HOT, CRITICAL, RELBF, VARIABLE, VTABLEFUNCS, VIRTFUNC, ALIASEE, REFS,
	TYPEIDINFO, TYPETESTS, TYPETESTASSUMEVCALLS, TYPECHECKEDLOADVCALLS, TYPETESTASSUMECONSTVCALLS,
	TYPECHECKEDLOADCONSTVCALLS, VFUNCID, OFFSET, ARGS, TYPEID, TYPEIDCOMPATIBLEVTABLE, SUMMARY, TYPETESTRES, KIND,
	UNSAT, BYTEARRAY, INLINE, SINGLE, ALLONES, SIZEM1BITWIDTH, ALIGNLOG2, SIZEM1, BITMASK, INLINEBITS, WPDRESOLUTIONS,
	WPDRES, INDIR, SINGLEIMPL, BRANCHFUNNEL, SINGLEIMPLNAME, RESBYARG, BYARG, UNIFORMRETVAL, UNIQUERETVAL,
	VIRTUALCONSTPROP, INFO, BYTE, BIT, VARFLAGS
}

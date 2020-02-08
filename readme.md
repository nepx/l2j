# `l2j` LLVM IR-to-Java compiler

`l2j` converts LLVM IR to Java bytecode. It's written in pure Java and has only one dependency -- the Jasmin bytecode assembler. 

## Project status

Currently generates Java bitcode from `test/simple.ll` without errors. 
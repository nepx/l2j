# `l2j` LLVM IR-to-Java compiler

`l2j` converts LLVM IR to Java bytecode. It contains a hand-written LLVM parser, a bytecode generator, and a partially-complete C runtime.

## Project status

It is able to parse a Hello World example generated by LLVM, but work on emitting the actual bytecode is ongoing. 
# I-language
I language compiler

A Java-based compiler for imperative language.
Uses hand-written lexical, syntax, and semantic analyzers.  

For code generation, translates I-language statements into Python code.

## Semantic analysis
Uses symbol-table approach for storing variables names and types.
The symbol-table is implemented in the form of a LinkedHashMap.
Each scope with body (routines, if, while-loops, for-loops) has its own symbol-table space for convenience of use and unambiguity
of variables action area.

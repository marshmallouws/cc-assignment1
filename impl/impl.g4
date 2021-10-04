grammar impl;

/* A small imperative language */

start   :  cs+=command* EOF ;

program : c=command                      # SingleCommand
	| '{' cs+=command* '}'           # MultipleCommands
	;
	
command : x=ID '=' e=expr ';'	         # Assignment
	| 'output' e=expr ';'            # Output
    | 'while' '('c=condition')' p=program  # WhileLoop
	| 'if' '(' c=condition ')' p=program ('else' pelse=program)? # IfStatement
	| 'for' '(' l=loopstuff ')' p=program # ForLoopStuff
	;
	
expr	: e1=expr '+' e2=expr # Addition
	| e1=expr '*' e2=expr # Multiplication
	| c=FLOAT     	      # Constant
	| x=ID		      # Variable
	| '(' e=expr ')'      # Parenthesis
	| name=ID '[' (idx=ID|num=NUM+) ']' # Array
	;

loopstuff : name=ID '=' (idx=ID|num=NUM+) '..' (idx1=ID|num1=NUM+) # LoopStuff;

condition : e1=expr comp=COMPARISON e2=expr # Conditional
	  ;  

ID    : ALPHA (ALPHA|NUM)* ;
FLOAT : '-'? NUM+ ('.' NUM+)? ;

COMPARISON : '==' | '>=' | '<=' | '>' | '<' | '!=' ; 

ALPHA : [a-zA-Z_ÆØÅæøå] ;
NUM   : [0-9] ;

WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'~[\n]*  -> skip;
COMMENT2   : '/*' (~[*] | '*'~[/]  )*   '*/'  -> skip;

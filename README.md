# Wordy

A toy programming language. We will do science to it.

## About the language

Wordy uses English words throughout its syntax. It supports simple arithmetic expressions, loops, and conditionals.

Wordy is intentially a very simple language. It supports a single type of value: 64-bit floating point numbers. A program operates on a set of named variables in a “context,” taking its input from any variables already present, and leaving its output as variables set at termination. Variables do not need to be declared, and all variables are zero by default.

There are no other types other than double, including boolean. Wordy's conditional allow a single comparison between two expressions; programs must construct “and” and “or” by using nested conditionals. The language has no traversible data structures, and is not Turing complete.

## Sample code

Compute the nth Fibonacci number, where `n` is provided as input:

```
Set a to 1.
Set b to 1.
Set count to n.
Loop:
    If count is less than 1 then exit loop.
    Set next to a plus b.
    Set a to b.
    Set b to next.
    Set count to count minus 1.
End of loop.
```

Compute a Mandelbrot orbit `count` at (`cr`, `ci`), assuming the count is infinite if it exceeds `max_iterations`:

```
Set zr to 0.
Set zi to 0.
Loop:
    If count is greater than max_iterations then:
        Set count to 1 divided by 0.
        Exit loop.
    End of conditional.
    If zr squared plus zi squared is greater than 4 then exit loop.
    Set new_zr to zr squared minus zi squared plus cr.
    Set zi to 2 times zr times zi plus ci.
    Set zr to new_zr.
    Set count to count plus 1.
End of loop.
```

## Language grammar

This is an informal grammar of the language. To help with readability, this grammar omits spaces. Details are in [the full parser](blob/master/src/wordy/parser/WordyParser.java).

```regex
Program →
    Block EOF

Block →
    (Statement ".")+

Statement →
    Assignment | Conditional | Loop | LoopExit

Conditional →
    "if" 
    Expression
    ("equals" | "is equal to" | "is less than" | "is greater than")
    Expression
    "then"
    (
        (":" Block ("else" ":" Block)? "end of conditional")
        | (Statement ("else" Statement)?)
    )

Loop →
    "loop" ":"
    Block
    "end of loop"

LoopExit →
    "exit loop"

Assignment →
    "set" Variable "to" Expression

Expression →
    AdditiveExpression

AdditiveExpression →
    MultiplicativeExpression (("plus" | "minus") MultiplicativeExpression)*

MultiplicativeExpression →
    ExponentialExpression (("times" | "divided by") ExponentialExpression)*

ExponentialExpression →
    Atom (("to the power of" ExponentialExpression) | "squared")*

Atom →
    Number | Variable | Parens

Parens →
    "(" Expression ")"

Number →
    "-"? Digit+ ("." Digit+)?

Variable →
    ([a-z] | [A-Z] | "_")+

Digit →
    [0-9]
```

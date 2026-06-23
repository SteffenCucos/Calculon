# Calculon

Calculon is an expression parser and evaluator for mathematical expressions. It was built as an early parser/evaluator project and supports common binary operators, grouping, and basic unary mathematical functions.

## Supported operations

| Syntax | Operation |
| --- | --- |
| `+` | Addition |
| `-` | Subtraction |
| `*` | Multiplication |
| `/` | Division |
| `^` | Exponentiation |
| `(...)` | Parentheses / grouping |
| `sin(...)` | Sine |
| `cos(...)` | Cosine |
| `tan(...)` | Tangent |
| `log(...)` | Logarithm |
| `ln(...)` | Natural logarithm |

## Example expressions

```text
1 + 2 * 3
(1 + 2) * 3
sin(0)
ln(10) + 4^2
```

## Project goals

- Tokenize mathematical expressions.
- Parse expressions with operator precedence.
- Evaluate expressions into numeric results.
- Provide a base for experimenting with interpreters, parsers, and expression trees.

## Running locally

Open the repository in the appropriate IDE for the project structure and run the main entry point. If a standard build tool is added later, document the exact command here.

## Status

Experimental / learning project.

## License

No license has been selected yet.

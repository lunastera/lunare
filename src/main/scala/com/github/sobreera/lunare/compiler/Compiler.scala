package com.github.sobreera.lunare.compiler

import com.github.sobreera.lunare.lexer.{Lexer, Token}
import com.github.sobreera.lunare.parser.{AST, Parser}

object Compiler {
  def apply(code: String): Either[CompilationError, AST] = {
    for {
      tokens <- Lexer(code)
      ast <- Parser(tokens)
    } yield ast
  }
}

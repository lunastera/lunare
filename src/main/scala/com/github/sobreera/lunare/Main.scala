package com.github.sobreera.lunare

import com.github.sobreera.lunare.compiler.{CompilationError, Compiler}
import com.github.sobreera.lunare.lexer.Lexer

object Main {
  def main(args: Array[String]): Unit = {
    val code =
      """
        |puts(123, "hoge")
      """.stripMargin.trim

    Lexer(code) match {
      case Right(tokens) => println(tokens)
      case Left(e) => println(e)
    }

    Compiler(code) match {
      case Right(ast)  => println(ast)
      case Left(error) => println(error)
    }

  }
}

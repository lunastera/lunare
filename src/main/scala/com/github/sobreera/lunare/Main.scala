package com.github.sobreera.lunare

import com.github.sobreera.lunare.compiler.{CompilationError, Compiler}

object Main {
  def main(args: Array[String]): Unit = {
    val code =
      """
        |"test"
      """.stripMargin.trim

    Compiler(code) match {
      case Right(ast)  => println(ast)
      case Left(error) => println(error)
    }
  }
}

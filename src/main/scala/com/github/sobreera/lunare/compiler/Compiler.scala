package com.github.sobreera.lunare.compiler

import com.github.sobreera.lunare.lexer.Lexer
import com.github.sobreera.lunare.parser.Parser

object Compiler {
  def apply(code: String): Either[CompilationError, Unit] = {
    for {
      tokens <- Lexer(code)
      ast <- Parser(tokens)
      bytecode <- BCodeCompiler(List(ast))
    } yield {
      val loader = new LunareClassLoader(classOf[LunareClassLoader].getClassLoader, Seq(""), "LunareMain", bytecode)
      val klass = Class.forName("LunareMain", true, loader)
      klass.getMethod("main", classOf[Array[String]]).invoke(null, Array(""))
    }
  }
}

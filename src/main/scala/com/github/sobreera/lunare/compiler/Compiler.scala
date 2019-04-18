package com.github.sobreera.lunare.compiler

import com.github.sobreera.lunare.lexer.Lexer
import com.github.sobreera.lunare.parser.Parser
import com.github.sobreera.lunare.runner.LunareRunner

object Compiler {
  def apply(code: String, config: Seq[String]): Either[CompilationError, Unit] = {
    for {
      tokens <- Lexer(code)
      ast <- Parser(tokens)
      bytecode <- BCodeCompiler(List(ast))
    } yield {
      new LunareRunner(new LunareClassLoader(classOf[LunareClassLoader].getClassLoader, Seq(""), "LunareMain", bytecode)).run()
    }
  }
}

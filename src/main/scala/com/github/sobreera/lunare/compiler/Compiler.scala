package com.github.sobreera.lunare.compiler

import java.io.{DataOutputStream, FileOutputStream}

import com.github.sobreera.lunare.lexer.Lexer
import com.github.sobreera.lunare.parser.Parser
import com.github.sobreera.lunare.runner.LunareRunner

object Compiler {
  def apply(code: String): Either[CompilationError, Unit] = {
    for {
      tokens <- Lexer(code)
      ast <- Parser(tokens)
      bytecode <- BCodeCompiler(ast)
    } yield {
//      debug(bytecode)
      new LunareRunner().run("LunareMain", bytecode)
    }
  }
  def debug(bytecode: Array[Byte]): Unit = {
    val out = new DataOutputStream(new FileOutputStream("LunareMain.class"))
    out.write(bytecode)
    out.flush()
    out.close()
  }
}

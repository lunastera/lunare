package com.github.sobreera.lunare

import com.github.sobreera.lunare.compiler.{Compiler, Location, ParserError}
import com.github.sobreera.lunare.parsing.AST.StringNode
import org.scalatest.{FlatSpec, Matchers}

class CompilerSpec extends FlatSpec with Matchers {
  private[this] val validCode =
    """
      |"test"
    """.stripMargin.trim

  private[this] val invalidCode =
    """
      |test
    """.stripMargin.trim

  val successAST = StringNode("test")
  val error = ParserError(Location(1, 1), "int node expected")

  "Lunare Compiler" should "success" in {
    Compiler(validCode) shouldBe Right(successAST)
  }

  it should "parse error" in {
    Compiler(invalidCode) shouldBe Left(error)
  }
}

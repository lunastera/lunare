package com.github.sobreera.lunare.parsing

import com.github.sobreera.lunare.compiler.{Location, ParserError}
import com.github.sobreera.lunare.lexer.{INT_LITERAL, STRING_LITERAL, Token}
import com.github.sobreera.lunare.parsing.AST.{IntNode, StringNode}

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

object Parser extends Parsers {
  override type Elem = Token

  class TokenReader(tokens: Seq[Token]) extends Reader[Token] {
    override def first: Token = tokens.head
    override def atEnd: Boolean = tokens.isEmpty
    override def pos: Position =
      tokens.headOption.map(_.pos).getOrElse(NoPosition)
    override def rest: Reader[Token] = new TokenReader(tokens.tail)
  }

  def apply(tokens: Seq[Token]): Either[ParserError, AST] = {
    val reader = new TokenReader(tokens)
    program(reader) match {
      case NoSuccess(msg, next) =>
        Left(ParserError(Location(next.pos.line, next.pos.column), msg))
      case Success(result, next) => Right(result)
    }
  }

  def program: Parser[AST] = positioned {
    phrase(expr)
  }

  def expr: Parser[AST] = positioned {
    phrase(stringNode | intNode)
  }

  private def stringNode: Parser[StringNode] = positioned {
    accept("string node", { case STRING_LITERAL(value) => StringNode(value.substring(1, value.length - 1)) })
  }
//  (optional(MINUS) map { if (it == null) 1 else -1}) * NUMBER map { (i, num) -> (i * num.text.toInt()).toString() }
  private def intNode: Parser[IntNode] = positioned {
    accept("int node", { case INT_LITERAL(value) => IntNode(value) })
  }
}

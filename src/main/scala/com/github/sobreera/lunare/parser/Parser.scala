package com.github.sobreera.lunare.parser

import com.github.sobreera.lunare.compiler.{Location, ParserError}
import com.github.sobreera.lunare.lexer._
import com.github.sobreera.lunare.parser.AST._

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
      case Success(result, _) => Right(result)
    }
  }

  def program: Parser[AST] = positioned {
    phrase(expr)
  }

  def expr: Parser[AST] = positioned {
    stringNode | intNode | functionCall
  }

  def functionCall: Parser[FunctionCall] = positioned {
    identifier ~ functionParameters ^^ {
      case name ~ params => FunctionCall(name.value, params)
    }
  }

  def functionParameters: Parser[List[AST]] = {
    (LPAR() ~> repsep(expr, COMMA()) <~ RPAR()) ^^ { list: List[AST] => list }
  }

  def block = positioned {
    LBRC() ~> expr.* <~ RBRC() ^^ { exprs: List[AST] =>  Block(exprs) }
  }

  def stringNode: Parser[StringNode] = positioned {
    accept("string", { case STRING_LITERAL(value) => StringNode(value.substring(1, value.length - 1)) })
  }

  def intNode: Parser[IntNode] = positioned {
    accept("int", { case INT_LITERAL(value) => IntNode(value) })
  }

  def identifier: Parser[IDENTIFIER] = positioned {
    accept("identifier", { case id @ IDENTIFIER(_) => id })
  }
}

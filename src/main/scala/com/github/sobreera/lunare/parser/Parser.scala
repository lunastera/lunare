package com.github.sobreera.lunare.parser

import com.github.sobreera.lunare.compiler.{Location, ParserError}
import com.github.sobreera.lunare.lexer._
import com.github.sobreera.lunare.parser.AST._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

object Parser extends Parsers {
  override type Elem = Token
  private val operator: Map[Token, AST.Operator] = Map(
    TIMES() -> Times(),
    DIV()   -> Div()
  )

  class TokenReader(tokens: Seq[Token]) extends Reader[Token] {
    override def first: Token = tokens.head
    override def atEnd: Boolean = tokens.isEmpty
    override def pos: Position =
      tokens.headOption.map(_.pos).getOrElse(NoPosition)
    override def rest: Reader[Token] = new TokenReader(tokens.tail)
  }

  def apply(tokens: Seq[Token]): Either[ParserError, List[AST]] = {
    val reader = new TokenReader(tokens)
    program(reader) match {
      case NoSuccess(msg, next) =>
        Left(ParserError(Location(next.pos.line, next.pos.column), msg))
      case Success(result, _) => Right(result)
    }
  }

  def program: Parser[List[AST]] = {
    statement.+
  }

  def statement: Parser[AST] = positioned {
    functionDeclaration
  }

  def expr: Parser[AST] = positioned {
    functionCall | stringNode | intNode
  }

  def functionDeclaration: Parser[FunctionDeclaration] = positioned {
    DEF() ~> identifier ~ functionParameters ~ block ^^ {
      case IDENTIFIER(name) ~ params ~ body => FunctionDeclaration(name, params, body)
    }
  }

  def functionParameters: Parser[List[Variable]] =
    (LPAR() ~> repsep(identifier, COMMA()) <~ RPAR()) ^^ {
      list: List[IDENTIFIER] => list.map(i => Variable(i.value))
    }

  def functionCall: Parser[FunctionCall] = positioned {
    identifier ~ functionCallParameters ^^ {
      case IDENTIFIER(name) ~ params => FunctionCall(name, params)
    }
  }

  def multiplicationOrExpr: Parser[AST] = positioned {
    val ope: Parser[Token] = TIMES() | DIV()
    chainl1(intNode, ope ^^ {
      case op => (l: AST, r: AST) => BinaryOperation(operator(op), l, r)
//      case DIV() => (l: AST, r: AST) => BinaryOperation(Div())
    })
  }

  def functionCallParameters: Parser[List[AST]] =
    (LPAR() ~> repsep(expr, COMMA()) <~ RPAR()) ^^ {
      list: List[AST] => list
    }

  def block: Parser[Block] = positioned {
    LBRC() ~> expr.* <~ RBRC() ^^ { exprs: List[AST] => Block(exprs) }
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

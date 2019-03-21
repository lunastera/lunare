package com.github.sobreera.lunare.lexer

import com.github.sobreera.lunare.compiler.{LexerError, Location}

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

object Lexer extends RegexParsers {
  override def skipWhitespace: Boolean = true
  override protected val whiteSpace: Regex = "[ \t\r\f]+".r

  def apply(code: String): Either[LexerError, List[Token]] =
    parse(tokens, code) match {
      case NoSuccess(msg, next) =>
        Left(LexerError(Location(next.pos.line, next.pos.column), msg))
      case Success(result, _) => Right(result)
    }

  def tokens: Parser[List[Token]] = {
    phrase(rep1(identifier | stringLiteral | intLiteral))
  }

  def identifier: Parser[IDENTIFIER] = positioned {
    "[a-zA-Z_][a-zA-Z0-9_]*".r ^^ { value =>
      IDENTIFIER(value)
    }
  }

  def stringLiteral: Parser[STRING_LITERAL] = positioned {
    "\".*?\"".r ^^ { value =>
      STRING_LITERAL(value)
    }
  }

  def intLiteral: Parser[INT_LITERAL] = positioned {
    "\\d+".r ^^ { value =>
      INT_LITERAL(value.toInt)
    }
  }
}

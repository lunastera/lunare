package com.github.sobreera.lunare.lexer

import scala.util.parsing.input.Positional

sealed trait Token extends Positional

case class IDENTIFIER(value: String) extends Token
case class STRING_LITERAL(value: String) extends Token
case class INT_LITERAL(value: Int) extends Token

case class LPAR() extends Token
case class RPAR() extends Token
case class LBRC() extends Token
case class RBRC() extends Token

case class COMMA() extends Token

case class FUNC() extends Token
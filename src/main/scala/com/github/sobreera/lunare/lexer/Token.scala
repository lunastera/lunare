package com.github.sobreera.lunare.lexer

import scala.util.parsing.input.Positional

sealed trait Token extends Positional

case class IDENTIFIER(value: String) extends Token
case class STRING_LITERAL(value: String) extends Token
case class INT_LITERAL(value: Int) extends Token

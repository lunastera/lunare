package com.github.sobreera.lunare.lexer

import scala.util.parsing.input.Positional

sealed trait Token extends Positional

case class IDENTIFIER(str: String) extends Token

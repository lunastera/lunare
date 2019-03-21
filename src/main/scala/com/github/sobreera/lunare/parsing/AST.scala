package com.github.sobreera.lunare.parsing

import scala.util.parsing.input.Positional

sealed trait AST extends Positional

object AST {
  case class StringNode(value: String) extends AST
  case class IntNode(value: Int) extends AST
}

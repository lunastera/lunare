package com.github.sobreera.lunare.parser

import scala.util.parsing.input.Positional

sealed trait AST extends Positional

object AST {
  case class StringNode(value: String) extends AST
  case class IntNode(value: Int) extends AST

  case class Block(list: List[AST]) extends AST
  case class Variable(name: String) extends AST

  case class BinaryOperation(operator: Operator, left: AST, right: AST) extends AST
  sealed trait Operator
  case object Times extends Operator
  case object Div extends Operator

  case class FunctionDeclaration(name: String, parameters: List[Variable], body: Block) extends AST
  case class FunctionCall(name: String, parameters: List[AST]) extends AST
}

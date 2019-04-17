package com.github.sobreera.lunare

import com.github.sobreera.lunare.compiler._

object Main {
  def main(args: Array[String]): Unit = {
    val code =
      """
        |func main() {
        | print("hoge")
        |}
      """.stripMargin.trim

    Compiler(code) match {
      case Right(_)  => {
        println("Success.")
      }
      case Left(error) => println(error)
    }
  }
}

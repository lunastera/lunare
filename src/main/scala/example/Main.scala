package example

import com.github.sobreera.lunare.compiler.Compiler

object Main {
  def main(args: Array[String]): Unit = {
    val code =
      """
        |func main() {
        | print("123")
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

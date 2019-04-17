package com.github.sobreera.lunare.compiler

import com.github.sobreera.lunare.parser.AST
import com.github.sobreera.lunare.parser.AST.{FunctionCall, FunctionDeclaration, IntNode, StringNode}
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes._

import scala.util.{Failure, Success, Try}

object BCodeCompiler {

  def apply(astList: List[AST]): Either[BCodeCompilerError, Array[Byte]] = {
    Try {
      compile(astList)
    } match {
      case Failure(exception) =>Left(BCodeCompilerError(exception.getMessage))
      case Success(value) => Right(value)
    }
  }

  def compile(astList: List[AST]): Array[Byte] = {
    implicit val cw: ClassWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
    cw.visit(V1_8,
      ACC_PUBLIC + ACC_SUPER,
      "LunareMain",
      null,
    "java/lang/Object",
      null)

    compileConstructor

    astList.foreach {
      case declaration: FunctionDeclaration => compileFunction(declaration)
    }

    cw.visitEnd()
    cw.toByteArray
  }

  def compileConstructor(implicit cw: ClassWriter): Unit = {
    val mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
    mv.visitVarInsn(ALOAD, 0)
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
    mv.visitInsn(RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()
  }

  def compileFunction(declaration: FunctionDeclaration)(implicit cw: ClassWriter): Unit = {
    val signature = "([Ljava/lang/String;)V"
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, declaration.name, signature, null, null)
    mv.visitCode()
    for(expr <- declaration.body.list) expr match {
      case FunctionCall(name, params) => {
        name match {
          case "print" => {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
            params.foreach {
              case StringNode(value) => mv.visitLdcInsn(value)
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false)
          }
        }
      }
    }
    mv.visitInsn(RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()
  }
}

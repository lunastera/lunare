package com.github.sobreera.lunare.runner

class LunareRunner() {
  def run(className: String, bytecode: Array[Byte]): Unit = {
    val loader = new LunareClassLoader(classOf[LunareClassLoader].getClassLoader, Seq(""), className, bytecode)
    val klass = Class.forName(className, true, loader)
    klass.getMethod("main", classOf[Array[String]]).invoke(null, Array(""))
  }
}

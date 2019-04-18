package com.github.sobreera.lunare.runner

class LunareRunner(loader: ClassLoader) {
  def run(): Unit = {
    val klass = Class.forName("LunareMain", true, loader)
    klass.getMethod("main", classOf[Array[String]]).invoke(null, Array(""))
  }
}

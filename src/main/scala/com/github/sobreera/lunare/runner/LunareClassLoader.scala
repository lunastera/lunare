package com.github.sobreera.lunare.runner

import java.io.File
import java.net.URLClassLoader

class LunareClassLoader(parent: ClassLoader, classPath: Seq[String], className: String, bytecode: Array[Byte])
  extends URLClassLoader(classPath.map(new File(_).toURI.toURL).toArray, parent) {

  defineClass(className, bytecode, 0, bytecode.length)

  override def findClass(name: String): Class[_] = super.findClass(name)
}

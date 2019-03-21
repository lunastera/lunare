val commonSettings = Def.settings(
  scalaVersion := "2.12.8"
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "lunare",
    version := "0.1.0-SNAPSHOT",
    organization := "com.github.sobreera",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
      "org.ow2.asm" % "asm" % "7.0",
      "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
    )
  )

import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.sobreera"

lazy val root = (project in file("."))
  .settings(
    name := "lunare",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
      "org.ow2.asm" % "asm" % "7.0",
      scalaTest % Test,
    )
  )

import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.sobreera"

lazy val root = (project in file("."))
  .settings(
    name := "lunare",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.ow2.asm" % "asm" % "7.0"
    )
  )

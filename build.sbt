enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

name := "my_blog"

version := "0.1"

scalaVersion := "2.12.8"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.3.1",
  "com.github.japgolly.scalajs-react" %%% "extra" % "1.3.1",
  "com.github.japgolly.scalacss" %%% "core" % "0.5.5",
  "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.5",
  "com.definitelyscala" %%% "scala-js-marked" % "1.1.0",
  "org.scalatest" %%% "scalatest" % "3.2.0-SNAP10" % Test
)

Compile / npmDependencies ++= Seq(
  "react"     -> "16.7.0",
  "react-dom" -> "16.7.0",
  "marked"    -> "0.5.2"
)

lazy val copyJs = TaskKey[Unit]("copyJs", "Copy generated files")
lazy val copyJsProd = TaskKey[Unit]("copyJsProd", "Copy production generated files")

copyJs := SbtUtils.copyGenerated(name.value, baseDirectory.value, mode = SbtUtils.RunMode.Dev)
copyJsProd := SbtUtils.copyGenerated(name.value, baseDirectory.value)

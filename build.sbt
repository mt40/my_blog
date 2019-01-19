enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

name := "my_blog"

version := "0.1"

scalaVersion := "2.12.8"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val scalaJS = Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    "com.github.japgolly.scalajs-react" %%% "core" % "1.3.1",
    "com.github.japgolly.scalajs-react" %%% "extra" % "1.3.1"
  )

  val facades = Seq(
    "org.querki" %%% "jquery-facade" % "1.2",
    "ru.pavkin" %%% "scala-js-momentjs" % "0.9.1"
  )

  val fp = Seq(
    "org.typelevel" %%% "cats-macros" % "1.5.0",
    "org.typelevel" %%% "cats-core" % "1.5.0",
    "org.typelevel" %%% "cats-kernel" % "1.5.0",
    "org.typelevel" %%% "cats-effect" % "1.1.0"
  )

  val utils = Seq(
    "com.softwaremill.sttp" %%% "core" % "1.5.2",
    "io.circe" %%% "circe-core" % "0.11.0",
    "io.circe" %%% "circe-generic" % "0.11.0",
    "io.circe" %%% "circe-generic-extras" % "0.11.0",
    "io.circe" %%% "circe-parser" % "0.11.0"
  )

  val testing = Seq(
    "org.scalatest" %%% "scalatest" % "3.2.0-SNAP10" % Test,
    "com.github.japgolly.scalajs-react" %%% "test" % "1.3.1" % Test
  )

  scalaJS ++ facades ++ fp ++ utils ++ testing
}

// Some other libs from NPM
Compile / npmDependencies ++= Seq(
  "react"     -> "16.7.0",
  "react-dom" -> "16.7.0",
  "marked"    -> "0.5.2",
  "jquery"    -> "3.3.1",
  "moment"    -> "2.19.2"
)

// Disable source maps generation
Compile / emitSourceMaps := false

/**
  * Because Heroku requires a Scala app to have a task named
  * 'stage' so that it could run 'sbt stage' to build the app.
  * But since we are already using 'npm' to build, we just add
  * the task to make Heroku happy, this task simply calls npm
  * build command.
  */
lazy val stage = TaskKey[Unit]("stage", "Dummy task for Heroku")
stage := {
  import scala.sys.process._
  "npm run build_production".!
}


// Configure style check when compile
Compile / wartremoverErrors ++= Seq(
  Wart.AsInstanceOf,
  Wart.EitherProjectionPartial,
  Wart.IsInstanceOf,
  Wart.Null,
  Wart.Product,
  Wart.Return,
  Wart.Serializable,
  Wart.StringPlusAny,
  Wart.TraversableOps,
  Wart.TryPartial,
  Wart.Var
)

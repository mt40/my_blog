addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.26")

// bundles the .js file emitted by the Scala.js compiler with its
// npm dependencies into a single .js file executable by Web browsers.
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.14.0")

// fast dependency resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")

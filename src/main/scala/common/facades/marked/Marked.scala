package common.facades.marked

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * ScalaJS facade for NPM 'marked'.
  *
  * @see https://www.npmjs.com/package/marked
  *      https://www.scala-js.org/doc/interoperability/facade-types.html
  */
@js.native
@JSImport("marked", JSImport.Namespace)
object Marked extends js.Object {

  /**
    * Compiles markdown to HTML.
    *
    * @param markdown string of markdown source to be compiled
    * @param options  extra options
    */
  def apply(markdown: String, options: Options = Options.default): String = js.native
}

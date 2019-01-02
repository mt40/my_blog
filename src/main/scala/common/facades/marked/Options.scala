package common.facades.marked

import scala.scalajs.js

/**
  * To use this, you must create a JS object literal like this:
  * {{{
  *   js.Dynamic.literal(key = value, ...).asInstanceOf[Options]
  * }}}
  */
@js.native
trait Options extends js.Object {

  /** Enable GitHub flavored markdown. */
  val gfm: Boolean = js.native

  /** Enable GFM tables. This option requires the gfm option to be true. */
  val tables: Boolean = js.native

  /**
    * A function to highlight code blocks.
    * The function takes three arguments: code, lang, and callback.
    */
  def highlight(code: String, lang: String, callback: js.Function = js.native): String = js.native
}

object Options {

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def default: Options = {
    js.Dynamic.literal(gfm = true, tables = true).asInstanceOf[Options]
  }
}

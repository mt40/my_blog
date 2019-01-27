package common.facades.highlighter

import japgolly.scalajs.react.Children
import japgolly.scalajs.react.JsComponent
import japgolly.scalajs.react.vdom.VdomNode

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * A component that wraps a code snippet transforms it into
  * html tags that can be styled. See `supportedLanguage` below for the list
  * of supported languages in this component.
  *
  * Code highlighting is backed by 'Highlight.js'. All available languages are in the link
  * below.
  *
  * @note To add more languages, pick them from the list of available languages and
  *       add to the webpack 'ContextReplacementPlugin' rule.
  *
  * @see Supported languages: https://highlightjs.org/static/demo/
  * @see Underlying component in Javascript: https://github.com/akiran/react-highlight
  */
object Highlighter {

  /**
    * Imports the optimized version for smaller code size.
    * @see https://github.com/akiran/react-highlight/issues/56
    */
  @js.native
  @JSImport(module = "react-highlight/lib/optimized", name = JSImport.Default)
  object Raw extends js.Object

  @js.native
  trait Props extends js.Object {
    val className: String = js.native

    val languages: js.Array[String] = js.native
  }

  private val component = JsComponent[Props, Children.Varargs, Null](Raw)

  private val supportedLanguage =
    js.Array("scala", "javascript", "html", "css", "scss", "java", "python", "bash")

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def apply(language: String)(children: VdomNode*) = {
    val props = js.Dynamic
      .literal(className = s"$language", language = supportedLanguage)
      .asInstanceOf[Props]
    component.apply(props)(children: _*)
  }
}

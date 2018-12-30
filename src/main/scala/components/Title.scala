package components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.language.postfixOps

object Title {

  object Style extends StyleSheet.Inline {
    import dsl._

    val font = fontFace("myFont3")(_.src("local(Pacifico)"))

    val self = style(
      fontSize(20 vw),
      fontFamily(font),
      color.white,
      // centering
      textAlign.center
    )
  }

  private val component = {
    ScalaComponent.builder
      .static("Title") {
        <.div(Style.self, "Hello!")
      }
      .build
  }

  def apply() = component()
}

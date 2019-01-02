package components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.postfixOps

object Tag {

  case class Props(name: String)

  private val component = {
    ScalaComponent
      .builder[Props]("Post")
      .render_P { props =>
        <.span(
          ^.cls := "tag",
          props.name
        )
      }
      .build
  }

  def apply(props: Props) = component(props)
}

package components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.postfixOps

object TagComp {

  case class Props(name: String)

  private val component = {
    ScalaComponent
      .builder[Props]("Tag")
      .render_P { props =>
        import japgolly.scalajs.react.vdom.all._
        span(
          cls := "tag padding-left-0 no-bg font-san-serif",
          span(cls := "tag-wall bg-gradient-1"),
          props.name
        )
      }
      .build
  }

  def apply(name: String) = component(Props(name))
}

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
        span(cls := "tag", props.name)
      }
      .build
  }

  def apply(name: String) = component(Props(name))
}
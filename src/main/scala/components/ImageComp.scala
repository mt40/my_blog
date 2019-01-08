package components

import japgolly.scalajs.react.ScalaComponent

object ImageComp {
  case class Props(url: String)

  private val component = {
    ScalaComponent
      .builder[Props]("Image")
      .render_P { props =>
        import japgolly.scalajs.react.vdom.all._

        figure(cls := "image is-16by9", img(src := props.url))
      }
      .build
  }

  def apply(url: String) = component(Props(url))
}

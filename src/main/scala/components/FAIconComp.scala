package components

import japgolly.scalajs.react.ScalaComponent

/**
  * A component for Font Awesome icons.
  *
  * @see https://fontawesome.com/
  */
object FAIconComp {
  case class Props(iconName: String)

  private val component = {
    ScalaComponent
      .builder[Props]("FAIcon")
      .render_P { props =>
        import japgolly.scalajs.react.vdom.all._
        span(cls := "icon", i(cls := props.iconName))
      }
      .build
  }

  def apply(iconName: String) =
    component(Props(iconName))
}

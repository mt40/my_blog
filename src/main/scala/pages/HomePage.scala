package pages

import components.Title
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.postfixOps

object HomePage {

  private val component = {
    ScalaComponent.builder
      .static("HomePage") {
        <.div(
          ^.cls := "hero is-primary is-fullheight",
          <.div(
            ^.cls := "hero-body",
            <.div(
              ^.cls := "container",
              <.div(Title())
            )
          )
        )
      }
      .build
  }

  def apply() = component()
}

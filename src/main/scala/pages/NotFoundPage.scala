package pages

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._

object NotFoundPage {
  private val component = {
    ScalaComponent
      .static("NotFoundPage") {
        <.div("Page not found")
      }
  }

  def apply() = component()
}

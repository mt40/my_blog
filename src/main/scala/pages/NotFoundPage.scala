package pages

import japgolly.scalajs.react.ScalaComponent

object NotFoundPage {
  private val component = {
    ScalaComponent
      .static("NotFoundPage") {
        import japgolly.scalajs.react.vdom.all._
        div("Page not found")
      }
  }

  def apply() = component()
}

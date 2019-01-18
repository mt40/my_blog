package pages

import japgolly.scalajs.react.ScalaComponent

object UnderConstructionPage {
  private val component = {
    ScalaComponent
      .static("UnderConstructionPage") {
        import japgolly.scalajs.react.vdom.all._

        section(
          cls := "hero is-fullheight is-warning is-bold",
          div(
            cls := "hero-body",
            div(
              cls := "container",
              h1(cls := "title font-8bit margin-bot-s", "Under construction")
            )
          )
        )
      }
  }

  def apply() = component()
}

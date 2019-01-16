package pages

import common.Api
import japgolly.scalajs.react.ScalaComponent

object PostNotFoundPage {
  private val component = {
    ScalaComponent
      .static("NotFoundPage") {
        import japgolly.scalajs.react.vdom.all._

        section(
          cls := "hero is-fullheight",
          div(
            cls := "hero-body",
            div(
              cls := "container",
              h1(cls := "title font-8bit margin-bot-s", "Post not found :("),
              a(
                cls := "is-size-5 font-8bit underline",
                href := Api.site.value,
                "<- back to home page"
              )
            )
          )
        )
      }
  }

  def apply() = component()
}

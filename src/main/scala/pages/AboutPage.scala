package pages

import components.AboutBlogComp
import japgolly.scalajs.react.ScalaComponent

object AboutPage {
  private val component = {
    ScalaComponent
      .static("AboutPage") {
        import japgolly.scalajs.react.vdom.all._

        div(
          shared.renderNavBar(),
          shared.bodyWrapper(
            AboutBlogComp(isTextSmall = false, isTextBlack = true)
          )
        )
      }
  }

  def apply() = component()
}

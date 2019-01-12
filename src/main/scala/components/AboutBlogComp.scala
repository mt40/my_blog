package components

import common.Config
import japgolly.scalajs.react.ScalaComponent

object AboutBlogComp {
  private val aboutThisBlog =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit.
      | Cras aliquet nunc nulla, non fermentum lacus tempor in.
      | Mauris placerat venenatis massa, sed rutrum magna placerat eu.
      | Maecenas aliquam, odio sit amet porta efficitur.""".stripMargin

  private val component = {
    ScalaComponent
      .static("AboutBlog") {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "content is-small has-text-grey",
          div(cls := "is-size-5 margin-bot-t", b("About this blog")),
          p(aboutThisBlog),
          div(cls := "is-size-6 underline", "Contact me"),
          a(
            href := s"mailto:${Config.authorEmail}",
            "GMail ",
            FAIconComp("far fa-envelope")
          )
        )
      }
  }

  def apply() = component()
}

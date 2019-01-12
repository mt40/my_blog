package components

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
          cls := "content is-small",
          h1(cls := "title margin-bot-t", "About this blog"),
          p(aboutThisBlog)
        )
      }
  }

  def apply() = component()
}

package components

import common.Config
import japgolly.scalajs.react.ScalaComponent

object AboutBlogComp {
  private val aboutThisBlog =
    """This blog saves the experience I have on the way trying to
      |become a better programmer. This includes introductions, tutorials,
      |career lessons, and bugs 🐞.
      |
      |If you are reading this, I hope that you will find something
      |useful for yourself.
      |
      |Happy coding 👨‍💻""".stripMargin

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
            "Gmail ",
            FAIconComp("far fa-envelope")
          ),
          div(cls := "is-size-6 underline", "Report issue"),
          a(
            href := s"https://github.com/mt40/my_blog/issues/new",
            "Github ",
            FAIconComp("fab fa-github")
          )
        )
      }
  }

  def apply() = component()
}

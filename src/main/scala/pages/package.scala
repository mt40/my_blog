import components.NavBarComp
import japgolly.scalajs.react.vdom.html_<^.VdomNode

package object pages {

  /** Programmatic representation of a page. */
  sealed trait PageType {
    def title: String
  }

  case object HomePageType extends PageType {
    override def title: String = "Home"
  }

  case object NotFoundPageType extends PageType {
    override def title: String = "Not found"
  }

  /** @param anchor id of a html tag to scroll to */
  case class FullPostPageType(postId: String, anchor: Option[String]) extends PageType {
    override def title: String = postId
  }

  object shared {

    def renderNavBar: VdomNode = {
      import japgolly.scalajs.react.vdom.all._

      section(
        cls := "section padding-top-0 padding-bot-0 bg-rain shadow-light",
        div(
          cls := "container",
          NavBarComp()
        )
      )
    }
  }
}

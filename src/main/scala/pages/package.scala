import components.NavBarComp
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.html_<^.VdomNode

package object pages {

  /** Programmatic representation of a page. */
  sealed trait PageType {
    def title: String
  }

  case object HomePageType extends PageType {
    override def title: String = "Home"
  }

  case object PostNotFoundPageType extends PageType {
    override def title: String = "Not found"
  }

  /** @param anchor id of a html tag to scroll to */
  case class FullPostPageType(postId: String, anchor: Option[String]) extends PageType {
    override def title: String = postId
  }

  case object UnderConstructionPageType extends PageType {
    override def title: String = "Under construction"
  }

  case object AboutPageType extends PageType {
    override def title: String = "About"
  }

  object shared {

    def renderNavBar(): VdomNode = doRenderNavBar(None)

    def renderNavBar(reloadDisqus: () => Callback): VdomNode =
      doRenderNavBar(Some(reloadDisqus))

    private def doRenderNavBar(reloadDisqus: Option[() => Callback]): VdomNode = {
      import japgolly.scalajs.react.vdom.all._

      section(
        cls := "section padding-top-0 padding-bot-0 shadow-light",
        div(
          cls := "container",
          reloadDisqus match {
            case Some(f) => NavBarComp(f)
            case _       => NavBarComp()
          }
        )
      )
    }

    def renderDisqus: VdomNode = {
      import japgolly.scalajs.react.vdom.all._
      div(id := "disqus_thread")
    }

    /**
      * Provides consistent spacing for the given elements.
      * Used this to wrap direct children of <body>
      */
    object bodyWrapper {

      def withMod(mods: TagMod*)(children: TagMod*): VdomNode = {
        import japgolly.scalajs.react.vdom.all._

        val container = div(
          cls := "container",
          div(
            cls := "columns",
            div(
              (cls := "column is-10 is-full-mobile is-offset-1") +: children: _*
            )
          )
        )

        section((cls := "section") +: mods :+ container: _*)
      }

      def apply(children: TagMod*): VdomNode = withMod()(children: _*)
    }
  }
}

import components.NavBarComp
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
        cls := "section padding-top-0 padding-bot-0 shadow-light",
        div(
          cls := "container",
          NavBarComp()
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
//    def bodyWrapper(children: TagMod*): VdomNode = {
//      import japgolly.scalajs.react.vdom.all._
//
//      section(
//        cls := "section",
//        div(
//          cls := "container",
//          div(
//            cls := "columns",
//            div(
//              (cls := "column is-10 is-full-mobile is-offset-1") +: children: _*
//            )
//          )
//        )
//      )
//    }

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

      def apply(children: TagMod*): VdomNode =  withMod()(children: _*)
    }
  }
}

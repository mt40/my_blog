package pages

import components.{HighlightPostComp, PostComp}
import core.content.{IOPostStore, Metadata, PostInfo}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps

object HomePage {

  case class Props(router: RouterCtl[PageType])

  case class State(metadata: Metadata)

  object State {
    lazy val default = State(Metadata.empty)
  }

  class Backend(scope: BackendScope[Props, State]) {

    def start: Callback = {
      Callback {
        IOPostStore.default.getMetadata
          .unsafeRunAsync {
            case Left(err) => println(err.toString)
            case Right(metadata) =>
              println("Successfully fetch metadata")
              scope.modState(_.copy(metadata = metadata)).runNow()
          }
      }
    }

    def render(props: Props, state: State) = {
      // If there are more than 3 posts, bring the 1st 2 posts
      // to the top as highlights
      val allPosts = state.metadata.postsInDescDate
      if(allPosts.length >= 3) {
        renderWithHighlights(props, state, allPosts)
      }
      else {
        renderNoHighlight(props, state, allPosts)
      }
    }

    def renderWithHighlights(props: Props, state: State, posts: Seq[PostInfo]): VdomNode = {
      val (highlights, others) = posts.splitAt(2) match {
        case (tops, remaining) =>
          val highlights = tops match {
            case Seq(first, second) =>
              (
                HighlightPostComp(first, props.router),
                HighlightPostComp(second, props.router)
              )
          }
          val others = remaining.toTagMod(PostComp(_, props.router))
          (highlights, others)
      }

      {
        import japgolly.scalajs.react.vdom.all._
        div(
          section(
            cls := "section",
            div(
              cls := "container",
              div(
                cls := "columns",
                div(
                  cls := "column is-8 is-offset-2",
                  div(
                    cls := "columns",
                    div(cls := "column is-half", highlights._1),
                    div(cls := "column is-half", highlights._2)
                  )
                )
              )
            )
          ),
          section(
            cls := "section",
            div(
              cls := "container",
              div(cls := "column is-8 is-offset-2", others)
            )
          )
        )
      }
    }

    def renderNoHighlight(props: Props, state: State, posts: Seq[PostInfo]): VdomNode = {
      val postComps = posts.toTagMod(PostComp(_, props.router))

      {
        import japgolly.scalajs.react.vdom.all._
        section(
          cls := "section",
          div(
            cls := "container",
            div(cls := "column is-8 is-offset-2", postComps)
          )
        )
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("HomePage")
      .initialState(State.default)
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .componentDidUpdate { c =>
        if(c.prevProps != c.currentProps) c.backend.start
        else Callback.empty
      }
      .build
  }

  def apply(router: RouterCtl[PageType]) = component(Props(router))
}

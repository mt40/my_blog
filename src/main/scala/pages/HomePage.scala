package pages

import components.PostComp
import core.content.{IOPostStore, Metadata}
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
      val posts = state.metadata.posts.toTagMod(PostComp(_, props.router))

      {
        import japgolly.scalajs.react.vdom.all._
        div(cls := "container", posts)
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("HomePage")
      .initialState(State.default)
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build
  }

  def apply(router: RouterCtl[PageType]) = component(Props(router))
}

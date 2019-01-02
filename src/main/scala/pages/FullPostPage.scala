package pages

import components.FullPost
import core.content.{Article, IOArticleStore}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps

object FullPostPage {

  case class Props(articleName: String, router: RouterCtl[PageType])

  case class State(article: Article)

  object State {
    lazy val default: State = State(Article.empty)
  }

  class Backend(scope: BackendScope[Props, State]) {

    def start(p: Props): Callback = {
      val store = IOArticleStore.default

      Callback {
        store.getMetadata
          .map(_.articles.find(_.name == p.articleName).get)
          .flatMap(store.get)
          .unsafeRunAsync {
            case Left(err) => println(err.toString)
            case Right(art) =>
              println("Successfully fetch full article")
              scope.modState(_.copy(article = art)).runNow()
          }
      }
    }

    def render(p: Props, s: State) = {
      <.div(
        ^.cls := "container",
        FullPost(s.article, p.router)
      )
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("FullPostPage")
      .initialState(State.default)
      .renderBackend[Backend]
      .componentDidMount(c => c.backend.start(c.props))
      .build
  }

  /**
    * When arrive at this page, the only reliable information is the article name
    * from the url. We use that to retrieved the metadata and the full article.
    */
  def apply(articleName: String, router: RouterCtl[PageType]) =
    component(Props(articleName, router))
}

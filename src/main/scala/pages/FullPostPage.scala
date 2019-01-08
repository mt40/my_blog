package pages

import components.{FullPostComp, SimilarList}
import core.content.{Article, ArticleInfo, IOArticleStore, Metadata}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps

object FullPostPage {

  case class Props(articleName: String, router: RouterCtl[PageType])

  case class State(article: Article, similar: Seq[ArticleInfo])

  object State {
    lazy val default: State = State(Article.empty, Seq.empty)
  }

  class Backend(scope: BackendScope[Props, State]) {

    def start(props: Props): Callback = {
      val store = IOArticleStore.default

      val getAndUpdateArticle = Callback {
        store.getMetadata
          .map(_.articles.find(_.name == props.articleName).get)
          .flatMap(store.get)
          .unsafeRunAsync {
            case Left(err) => println(err.toString)
            case Right(art) =>
              println("Successfully fetch full article")
              scope.modState(_.copy(article = art)).runNow()
          }
      }

      val getAndUpdateSimilarArticles = Callback {
        val getSimilar = store.getMetadata
          .map { metadata =>
            val info = metadata.articles.find(_.name == props.articleName).get
            val similar = getSimilarArticles(metadata, info)
            similar
          }

        getSimilar
          .unsafeRunAsync {
            case Left(err)  => println(err.toString)
            case Right(sml) => scope.modState(_.copy(similar = sml)).runNow()
          }
      }

      getAndUpdateArticle >> getAndUpdateSimilarArticles
    }

    /**
      * Returns the top 3 articles with most common tags with the given
      * article.
      */
    def getSimilarArticles(metadata: Metadata, art: ArticleInfo): Seq[ArticleInfo] = {
      metadata.articles
        .map { a =>
          val commonTags = a.tags.intersect(art.tags).distinct.length
          a -> commonTags
        }
        .sortBy(-_._2) // sort descending
        .take(3)
        .map(_._1)
    }

    def render(props: Props, state: State): VdomNode = {
      import japgolly.scalajs.react.vdom.all._

      div(
        cls := "container",
        FullPostComp(state.article),
        SimilarList(state.similar, props.router)
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

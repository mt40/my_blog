package pages

import components.{FullPostComp, SimilarList}
import core.content.{IOPostStore, Metadata, Post, PostInfo}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps

object FullPostPage {

  case class Props(postId: String, router: RouterCtl[PageType])

  case class State(post: Post, similar: Seq[PostInfo])

  object State {
    lazy val default: State = State(Post.empty, Seq.empty)
  }

  class Backend(scope: BackendScope[Props, State]) {

    def start(props: Props): Callback = {
      val store = IOPostStore.default

      val getAndUpdatePost = Callback {
        store.getMetadata
          .map(_.posts.find(_.id == props.postId).get)
          .flatMap(store.get)
          .unsafeRunAsync {
            case Left(err) => println(err.toString)
            case Right(post) =>
              println("Successfully fetch full post")
              scope.modState(_.copy(post = post)).runNow()
          }
      }

      val getAndUpdateSimilarPosts = Callback {
        val getSimilar = store.getMetadata
          .map { metadata =>
            val info = metadata.posts.find(_.id == props.postId).get
            val similar = getSimilarPosts(metadata, info)
            similar
          }

        getSimilar
          .unsafeRunAsync {
            case Left(err)  => println(err.toString)
            case Right(sml) => scope.modState(_.copy(similar = sml)).runNow()
          }
      }

      getAndUpdatePost >> getAndUpdateSimilarPosts
    }

    /**
      * Returns the top 3 posts with most common tags with the given
      * post.
      */
    def getSimilarPosts(metadata: Metadata, info: PostInfo): Seq[PostInfo] = {
      metadata.posts
        .filterNot(_ sameAs info) // exclude current post
        .map { p =>
          val commonTags = p.tags.intersect(info.tags).distinct.length
          (p, commonTags)
        }
        .sortBy(-_._2) // sort descending
        .take(3)
        .map(_._1)
    }

    def render(props: Props, state: State): VdomNode = {
      import japgolly.scalajs.react.vdom.all._

      div(
        cls := "container",
        FullPostComp(state.post),
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
      .componentDidUpdate { c =>
        if(c.prevProps != c.currentProps) c.backend.start(c.currentProps)
        else Callback.empty
      }
      .build
  }

  /**
    * When arrive at this page, the only reliable information is the post id
    * from the url. We use that to retrieved the metadata and the full post.
    */
  def apply(postId: String, router: RouterCtl[PageType]) =
    component(Props(postId, router))
}

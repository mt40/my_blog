package pages

import common.Config
import components.{FullPostComp, PostContentComp}
import core.content.{IOPostStore, Metadata, Post, PostInfo}
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, React, ScalaComponent}

import scala.language.postfixOps
import scala.util.Try

object FullPostPage {

  case class Props(postId: String, anchor: Option[String], currentUrl: String)

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

              // if there is an internal link, scroll to the target element
              props.anchor foreach scrollTo
          }
      }

      val getAndUpdateSimilarPosts = Callback {
        val getSimilar = store.getMetadata
          .map { metadata =>
            val info = metadata.posts.find(_.id == props.postId).get
            val similar = getSimilarPosts(metadata, info)
            similar
          }

        getSimilar.unsafeRunAsync {
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
        .take(Config.similarPostsCount)
        .map(_._1)
    }

    def render(props: Props, state: State): VdomNode = {
      import japgolly.scalajs.react.vdom.all._

      val similarPosts = {
        val posts = state.similar.toTagMod { post =>
          div(cls := "column is-4", PostContentComp(post, isSmall = true))
        }
        div(
          cls := "columns",
          posts
        )
      }

      React.Fragment(
        shared.renderNavBar,
        section(
          cls := "section",
          div(
            cls := "container",
            div(
              cls := "columns",
              div(
                cls := "column is-10 is-full-mobile is-offset-1",
                FullPostComp(state.post, props.currentUrl)
              )
            )
          )
        ),
        section(
          cls := "section has-background-light",
          div(
            cls := "container",
            div(
              cls := "columns",
              div(
                cls := "column is-10 is-full-mobile is-offset-1",
                similarPosts
              )
            )
          )
        )
      )
    }

    private def scrollTo(id: String) = {
      Try {
        import common.Global.jQuery
        val elem = jQuery(id)
        println(s"Scroll to '$id'")
        val offset = elem.offset().top - 100 // leave some space above
        jQuery("html").scrollTop(offset)
      } recover {
        case err => println(s"Cannot scroll to '$id'.\n$err")
      }
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
  def apply(postId: String, anchor: Option[String], currentUrl: String) = {
    component(Props(postId, anchor, currentUrl))
  }
}

package pages

import common.Global.jQuery
import common.{Api, Config}
import components.{FullPostComp, PostContentComp}
import core.content.{IOPostStore, Metadata, Post, PostInfo}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps
import scala.util.Try

object FullPostPage {

  case class Props(
    postId: String,
    anchor: Option[String],
    currentUrl: String,
    router: RouterCtl[PageType]
  )

  case class State(post: Post, similar: Seq[PostInfo])

  object State {
    lazy val default: State = State(Post.empty, Seq.empty)
  }

  private val disqusScriptClass = "disqus-script"

  class Backend(scope: BackendScope[Props, State]) {

    def start(props: Props): Callback = {
      val store = IOPostStore.default

      val getAndUpdatePost = Callback {
        store.getMetadata
          .map(_.posts.find(_.id == props.postId).get)
          .flatMap(store.get)
          .unsafeRunAsync {
            case Left(err) => {
              println(err.toString)
              props.router.set(PostNotFoundPageType).runNow()
            }
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

      val postContentSection = shared.bodyWrapper(FullPostComp(state.post, props.currentUrl))

      val similarPostsSection = {
        val similarPosts = {
          val posts = state.similar.toTagMod { post =>
            div(cls := "column is-4", PostContentComp(post, isSmall = true))
          }
          div(
            cls := "columns",
            posts
          )
        }

        shared.bodyWrapper
          .withMod(cls := "has-background-light")(similarPosts)
      }

      val comments = shared.bodyWrapper
        .withMod(cls := "has-background-light padding-top-0")(shared.renderDisqus)

      div(
        shared.renderNavBar,
        postContentSection,
        similarPostsSection,
        comments
      )
    }

    private def scrollTo(id: String) = {
      Try {
        val elem = jQuery(id)
        println(s"Scroll to '$id'")
        val offset = elem.offset().top - 100 // leave some space above
        jQuery("html").scrollTop(offset)
      } recover {
        case err => println(s"Cannot scroll to '$id'.\n$err")
      }
    }

    /**
      * Loads the comment section using jQuery. This script is taken directly
      * from Disqus (see link below).
      * The decision to do it like this is because:
      * <ul>
      *   <li> adding this script tag from React doesn't work, the script is not
      * executed
      *   <li> there is a js component for it from the link below but implementing
      * the facade for this is complicated
      *   <li> we want the script to load after the page is mounted and this
      * does just that
      * </ul>
      *
      * @see https://disqus.com/admin/universalcode/
      *      https://www.npmjs.com/package/disqus-react
      */
    def loadComments(postId: String) = Callback {
      Try {
        // remove previously inserted scripts
        jQuery(s"body > .$disqusScriptClass").remove()

        val pageUrl = (Api.site / "#!post" / postId).value // Api.post(postId).value
        val identifier = postId
        val script =
          s"""
             |<script class="$disqusScriptClass">
             |console.log("loading Disqus with url='$pageUrl', id='$identifier'");
             |var disqus_config = function() {
             |this.page.url = '$pageUrl';
             |this.page.identifier = '$identifier';
             |};
             |/* DON'T EDIT BELOW THIS LINE */
             |(function() {
             |var d = document, s = d.createElement('script');
             |s.src = 'https://undertherain.disqus.com/embed.js';
             |s.setAttribute('data-timestamp', +new Date());
             |(d.head || d.body).appendChild(s);
             |})();
             |</script>
          """.stripMargin
        val noScript =
          s"""
            |<noscript class="$disqusScriptClass">
            |  "Please enable JavaScript to view the ",
            |  <a href := "https://disqus.com/?ref_noscript">"comments powered by Disqus."</a>
            |</noscript>""".stripMargin
        jQuery("body").append(script).append(noScript)
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("FullPostPage")
      .initialState(State.default)
      .renderBackend[Backend]
      .componentDidMount { c =>
        c.backend.start(c.props) >> c.backend.loadComments(c.props.postId)
      }
      .componentDidUpdate { c =>
        if(c.prevProps != c.currentProps) {
          c.backend.start(c.currentProps) >> c.backend.loadComments(c.currentProps.postId)
        }
        else Callback.empty
      }
      .build
  }

  /**
    * When arrive at this page, the only reliable information is the post id
    * from the url. We use that to retrieved the metadata and the full post.
    */
  def apply(
    postId: String,
    anchor: Option[String],
    currentUrl: String,
    router: RouterCtl[PageType]
  ) = {
    component(Props(postId, anchor, currentUrl, router))
  }
}

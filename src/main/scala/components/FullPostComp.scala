package components

import common.Api
import common.Global.jQuery
import core.content.Post
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps

object FullPostComp {

  /**
    * @param currentUrl url of the current page. This is required
    *                   to create anchors to link to a part of this
    *                   component
    */
  case class Props(post: Post, currentUrl: String)

  /**
    * Marks the tag that contains the full post content.
    * This is useful if we need to do manual modification
    * to improve the html representing the content.
    */
  private val fullContentClassName = "full-content"
  private val anchorClassName = "post-anchor"
  private val imageClassName = "content-img"
  private val imageWrapperClassName = "content-img-wrapper"

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props): VdomNode = {
      val tags = props.post.info.tags.toTagMod(TagComp(_))

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          div(
            cls := s"content $fullContentClassName",
            h1(cls := "margin-bot-m", props.post.info.title),
            div(dangerouslySetInnerHtml := props.post.html)
          ),
          div(
            cls := "is-italic has-text-grey-light margin-top-l",
            s"Published on ${props.post.info.createDate.prettyString}"
          ),
          div(
            cls := "is-size-3 has-text-grey-light margin-bot-m",
            "- - -"
          ),
          div(cls := "tags margin-bot-t", tags)
        )
      }
    }

    /**
      * Edits the html of the post content to improve UI
      * and add some functionality.
      * Since this is just an extra feature, we swallow all errors
      * so that the app doesn't crash.
      */
    def editContentHtml(props: Props): Callback = {
      val cb = for {
        _ <- addAnchors(props)
        _ <- fixImageUrls(props)
      } yield ()

      cb.attempt.map {
        case Left(err) => println(err)
        case Right(_)  =>
      }
    }

    /**
      * Adds anchors that allow jumping to headers in the page.
      * Only tag 'h2' will be updated since we don't want
      * to pollute the UI.
      */
    private def addAnchors(props: Props): Callback = Callback {
      val headers = jQuery(s".$fullContentClassName h2")
        .filter(":not(:has(a))") // already added

      headers.foreach { header =>
        val originalText = header.textContent
        val headerId = s"#${header.id}"
        val ref = {
          val url = props.currentUrl
          // if url already has header id, remove it
          val sanitized = if(url.count(_ == '#') >= 2) {
            url.splitAt(url.lastIndexOf('#'))._1
          }
          else {
            url
          }
          sanitized + headerId
        }
        val anchor = s"""<a class="$anchorClassName" href="$ref"># </a>"""
        header.innerHTML = s"""$anchor$originalText"""
      }
    }

    /**
      * Add these fixes:
      * - Replace existing url with an absolute url
      * - Wrap 'img' tag in a 'div' to help styling.
      */
    private def fixImageUrls(props: Props): Callback = Callback {
      // select all 'img' tags with attribute 'alt' starts with a prefix
      // indicating that this image should be served by us
      val images = jQuery(s".$fullContentClassName img")
        .filter("[alt^='image.']")
      images foreach { img =>
        val originalUrl = img.getAttribute("src")
        val correctUrl = Api.postResource(props.post.info.id, originalUrl).value
        img.setAttribute("src", correctUrl)
        img.setAttribute("class", imageClassName)
      }
      images.wrap(s"""<div class="$imageWrapperClassName"></div>""")
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("FullPost")
      .renderBackend[Backend]
      .componentDidMount(c => c.backend.editContentHtml(c.props))
      .componentDidUpdate { c =>
        if(c.prevProps != c.currentProps) c.backend.editContentHtml(c.currentProps)
        else Callback.empty
      }
      .build
  }

  def apply(post: Post, currentUrl: String) =
    component(Props(post, currentUrl))
}

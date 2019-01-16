package components

import common.Global.jQuery
import core.content.Post
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import scala.language.postfixOps
import scala.util.Try

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
      * Adds anchors that allow jumping to headers in the page.
      * Only tag 'h2' will be updated since we don't want
      * to pollute the UI.
      */
    def addAnchors(props: Props): Callback = Callback {
      Try {
        val headers = jQuery(s".$fullContentClassName h2")
          .filter(":not(:has(a))") // already added

        headers.foreach { header =>
          val originalText = header.textContent
          val headerId = s"#${header.id}"
          val ref ={
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
      } recover {
        // don't crash because of this
        case err => println(err)
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("FullPost")
      .renderBackend[Backend]
      .componentDidMount(c => c.backend.addAnchors(c.props))
      .componentDidUpdate { c =>
        if(c.prevProps != c.currentProps) c.backend.addAnchors(c.currentProps)
        else Callback.empty
      }
      .build
  }

  def apply(post: Post, currentUrl: String) =
    component(Props(post, currentUrl))
}

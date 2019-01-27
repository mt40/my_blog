package components

import core.content.{Post, Translator}
import core.markdown.MarkdownParser
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps
import scala.util.Failure

object FullPostComp {

  case class Props(post: Post)

  /**
    * Marks the tag that contains the full post content.
    * This is useful if we need to do manual modification
    * to improve the html representing the content.
    */
  private val fullContentClassName = "full-content"

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props): VdomNode = {
      val tags = props.post.info.tags.toTagMod(TagComp(_))

      {
        import japgolly.scalajs.react.vdom.all._

        val postBody = MarkdownParser
          .parse(props.post.markdown)
          .map(Translator.apply(_, props.post.info))
          .recoverWith {
            case err: Throwable =>
              println(err)
              Failure(err)
          }
          .getOrElse(EmptyVdom)

        div(
          div(
            cls := s"content $fullContentClassName",
            h1(cls := "margin-bot-m", props.post.info.title),
            postBody
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
  }

  private val component = {
    ScalaComponent
      .builder[Props]("FullPost")
      .renderBackend[Backend]
      .build
  }

  def apply(post: Post) =
    component(Props(post))
}

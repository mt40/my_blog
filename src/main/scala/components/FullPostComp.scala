package components

import core.content.Post
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps

object FullPostComp {

  case class Props(post: Post)

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      val tags = props.post.info.tags.toTagMod(TagComp(_))

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          div(
            cls := "content",
            dangerouslySetInnerHtml := props.post.html
          ),
          div(
            cls := "is-italic has-text-grey-light margin-top-l",
            s"Published on ${props.post.info.createDate}"
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

  def apply(post: Post) = component(Props(post))
}

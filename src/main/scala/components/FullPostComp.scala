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
          div(props.post.info.createDate),
          div(cls := "tags", tags)
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

package components

import core.content.Article
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps

object FullPostComp {

  case class Props(art: Article)

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      val tags = props.art.info.tags.toTagMod(TagComp(_))

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          div(
            cls := "content",
            dangerouslySetInnerHtml := props.art.html
          ),
          div(props.art.info.createDate),
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

  def apply(art: Article) = component(Props(art))
}

package components

import core.content.Article
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps

object FullPost {

  case class Props(art: Article)

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      val tags = p.art.info.tags.toTagMod(t => Tag(Tag.Props(t)))

      <.div(
        <.div(
          ^.cls := "content",
          ^.dangerouslySetInnerHtml := p.art.html
        ),
        <.div(p.art.info.createDate),
        <.div(^.cls := "tags", tags)
      )
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("Post")
      .renderBackend[Backend]
      .build
  }

  def apply(art: Article) = component(Props(art))
}

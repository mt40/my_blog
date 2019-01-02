package components

import core.content.Article
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages._

import scala.language.postfixOps

object FullPost {

  case class Props(art: Article, router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      val tags = p.art.info.tags.toTagMod(t => Tag(Tag.Props(t)))

      <.div(
        ^.cls := "content",
        p.art.html,
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

  def apply(art: Article, router: RouterCtl[PageType]) =
    component(Props(art, router))
}

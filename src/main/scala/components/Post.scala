package components

import common.Api
import core.content.ArticleInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages._

import scala.language.postfixOps

object Post {

  case class Props(info: ArticleInfo, router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      val tags = p.info.tags.toTagMod(t => Tag(Tag.Props(t)))

      val image = p.info.image.map { imgUrl =>
        <.figure(
          ^.cls := "image is-16by9",
          <.img(^.src := Api.articleResource(imgUrl).value)
        )
      }
      val summary = p.info.summary.map(sum => <.div(sum))

      <.article(
        ^.cls := "media",
        <.div(
          ^.cls := "media-content",
          <.div(
            ^.cls := "content",
            p.router setOnClick FullPostPageType(p.info.name),
            <.h1(^.cls := "title", p.info.name),
            image,
            summary
          ),
          <.div(p.info.createDate),
          <.div(^.cls := "tags", tags)
        )
      )
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("Post")
      .renderBackend[Backend]
      .build
  }

  def apply(info: ArticleInfo, router: RouterCtl[PageType]) =
    component(Props(info, router))
}

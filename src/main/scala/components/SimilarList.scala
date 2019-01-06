package components

import common.Api
import core.content.ArticleInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages.{FullPostPageType, PageType}

import scala.language.postfixOps

object SimilarList {

  case class Props(similarArticles: Seq[ArticleInfo], router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      val tags = p.similarArticles.toTagMod { art =>
        val image = art.image.map { imgUrl =>
          <.figure(
            ^.cls := "image is-16by9",
            <.img(^.src := Api.articleResource(imgUrl).value)
          )
        }
        val summary = art.summary.map(sum => <.div(sum))

        <.div(
          ^.cls := "tile is-parent",
          <.div(
            ^.cls := "tile is-child box",
            <.article(
              ^.cls := "media",
              <.div(
                ^.cls := "media-content",
                <.div(
                  ^.cls := "content",
                  p.router setOnClick FullPostPageType(art.name),
                  <.h1(^.cls := "title", art.name),
                  image,
                  summary
                )
              )
            )
          )
        )
      }

      <.div(
        ^.cls := "tile is-ancestor",
        tags
      )
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("Post")
      .renderBackend[Backend]
      .build
  }

  def apply(similarArticles: Seq[ArticleInfo], router: RouterCtl[PageType]) =
    component(Props(similarArticles, router))
}

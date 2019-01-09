package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import pages.{FullPostPageType, PageType}

import scala.language.postfixOps

object SimilarPost {

  case class Props(info: PostInfo, router: RouterCtl[PageType])

  private val component = {
    ScalaComponent
      .builder[Props]("SimilarPost")
      .render_P { props =>
        val info = props.info
        val image = info.image.map { img =>
          val url = Api.postResource(img).value
          ImageComp(url)
        }

        {
          import japgolly.scalajs.react.vdom.all._

          div(
            cls := "tile is-parent",
            div(
              cls := "tile is-child box",
              article(
                cls := "media",
                div(
                  cls := "media-content",
                  div(
                    cls := "content",
                    props.router setOnClick FullPostPageType(info.name),
                    h1(cls := "title", info.name),
                    image,
                    info.summary.map(div(_))
                  )
                )
              )
            )
          )
        }
      }
      .build
  }

  def apply(info: PostInfo, router: RouterCtl[PageType]) =
    component(Props(info, router))
}

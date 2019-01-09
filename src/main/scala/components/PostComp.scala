package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages._

import scala.language.postfixOps

object PostComp {

  case class Props(info: PostInfo, router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      val info = props.info
      val tags = info.tags.toTagMod(TagComp(_))
      val image = info.image.map { img =>
        val url = Api.postResource(img).value
        ImageComp(url)
      }

      {
        import japgolly.scalajs.react.vdom.all._

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
            ),
            div(info.createDate),
            div(cls := "tags", tags)
          )
        )
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("Post")
      .renderBackend[Backend]
      .build
  }

  def apply(info: PostInfo, router: RouterCtl[PageType]) =
    component(Props(info, router))
}

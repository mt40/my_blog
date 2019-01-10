package components

import core.content.PostInfo
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import pages.PageType

import scala.language.postfixOps

object HighlightPostComp {

  case class Props(info: PostInfo, router: RouterCtl[PageType])

  private val component = {
    ScalaComponent
      .builder[Props]("HighlightPost")
      .render_P { props =>
        val info = props.info
        val image = ImageComp(info)
        val tags = info.tags.toTagMod(TagComp(_))

        {
          import japgolly.scalajs.react.vdom.all._

          div(
            image,
            PostContentComp(info),
            div(cls := "tags", tags)
          )
        }
      }
      .build
  }

  def apply(info: PostInfo, router: RouterCtl[PageType]) =
    component(Props(info, router))
}

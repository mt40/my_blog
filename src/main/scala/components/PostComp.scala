package components

import core.content.PostInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages._

import scala.language.postfixOps

object PostComp {

  case class Props(info: PostInfo, router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props): VdomNode = {
      val info = props.info
      val tags = info.tags.toTagMod(TagComp(_))

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "margin-bot-m",
          ImageComp(info),
          PostContentComp(info),
          div(cls := "tags", tags)
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

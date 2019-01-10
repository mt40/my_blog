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
      val content: VdomNode =
        if(info.image.isEmpty) PostContentComp(info) else renderWithImage(info)

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "margin-bot-m",
          content,
          div(cls := "tags", tags)
        )
      }
    }

    private def renderWithImage(info: PostInfo): VdomNode = {
      val image = ImageComp(info)

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "columns",
          div(
            cls := "column is-7",
            image
          ),
          div(
            cls := "column is-5",
            PostContentComp(info)
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

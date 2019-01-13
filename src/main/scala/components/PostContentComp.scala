package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps

object PostContentComp {

  case class Props(info: PostInfo, isSmall: Boolean)

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props): VdomNode = {
      val info = props.info
      val fullPostUrl = Api.post(info.id).value
      val titleSize = if(props.isSmall) Some("is-size-5") else None

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "content margin-bot-s",
          div(
            cls := "margin-bot-t",
            a(
              cls := "title is-size-4-mobile",
              cls :=? titleSize,
              href := fullPostUrl,
              info.title
            )
          ),
          info.summary.map(div(cls := "is-size-6", _))
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

  def apply(info: PostInfo, isSmall: Boolean = false): Unmounted[Props, Unit, Backend] =
    component(Props(info, isSmall))
}

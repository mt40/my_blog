package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}

import scala.language.postfixOps

object PostContentComp {

  case class Props(info: PostInfo)

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props): VdomNode = {
      val info = props.info
      val fullPostUrl = Api.post(info.id).value

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "content",
          a(
            cls := "title",
            href := fullPostUrl,
            info.title
          ),
          br,
          br,
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

  def apply(info: PostInfo): Unmounted[Props, Unit, Backend] =
    component(Props(info))
}

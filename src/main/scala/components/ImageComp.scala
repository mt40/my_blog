package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted

object ImageComp {
  case class Props(url: String)

  private val component = {
    ScalaComponent
      .builder[Props]("Image")
      .render_P { props =>
        import japgolly.scalajs.react.vdom.all._
        figure(
          cls := "image is-16by9 margin-bot-s",
          img(cls := "border-light", src := props.url)
        )
      }
      .build
  }

  def apply(url: String): Unmounted[Props, Unit, Unit] =
    component(Props(url))

  def apply(info: PostInfo): Option[Unmounted[Props, Unit, Unit]] = {
    info.image.map { img =>
      val url = Api.postResource(info.id, img).value
      ImageComp(url)
    }
  }
}

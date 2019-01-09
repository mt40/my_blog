package components

import common.Api
import core.content.PostInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages._
import scalacss.DevDefaults.{StyleA, _}
import scalacss.ScalaCssReact._
import scalacss.internal.FontFace
import scalacss.internal.mutable.StyleSheet

import scala.language.postfixOps

object PostComp {

  case class Props(info: PostInfo, router: RouterCtl[PageType])

  object Style extends StyleSheet.Inline {
    import dsl._

    val font: FontFace[String] = fontFace("myFont")(_.src("local(Pacifico)"))

    val self: StyleA = style(
      fontFamily(font),
      marginBottom(8 vh)
    )
  }

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      val info = props.info
      val tags = info.tags.toTagMod(TagComp(_))
      val image = info.image.map { img =>
        val url = Api.postResource(info.id, img).value
        ImageComp(url)
      }
      val createDate = s"Posted on ${info.createDate}"
      val fullPostUrl = Api.post(info.id).value

      {
        import japgolly.scalajs.react.vdom.all._

        div(
          cls := "media",
          Style.self,
          div(
            cls := "media-content",
            div(
              cls := "content",
              a(
                cls := "title is-size-1",
                href := fullPostUrl,
                info.title
              ),
              image,
              info.summary.map(div(cls := "is-size-5", _))
            ),
            div(cls := "tags", tags),
            div(createDate)
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

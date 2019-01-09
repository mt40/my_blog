package components

import core.content.PostInfo
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import pages.PageType

import scala.language.postfixOps

object SimilarList {

  case class Props(similarPosts: Seq[PostInfo], router: RouterCtl[PageType])

  class Backend(scope: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      val tags = props.similarPosts.toTagMod(SimilarPost(_, props.router))

      import japgolly.scalajs.react.vdom.all._
      div(cls := "tile is-ancestor", tags)
    }
  }

  private val component = {
    ScalaComponent
      .builder[Props]("SimilarList")
      .renderBackend[Backend]
      .build
  }

  def apply(similarPosts: Seq[PostInfo], router: RouterCtl[PageType]) =
    component(Props(similarPosts, router))
}
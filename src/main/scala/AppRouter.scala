import common.Config
import japgolly.scalajs.react.extra.router.StaticDsl.Route
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import pages.{HomePage, _}

object AppRouter {

  private val routerConfig = RouterConfigDsl[PageType].buildConfig { dsl =>
    import dsl._

    // "/" -> home page
    val home = staticRoute(root, HomePageType) ~> renderR(HomePage.apply)

    // "/post/<post_name>" -> full article page
    val fullPost = {
      val route: Route[FullPostPageType] =
        ("post" / string("""[a-z0-9_\-]+""")).caseClass[FullPostPageType]
      dynamicRouteCT(route) ~>
        dynRenderR((page, router) => FullPostPage(page.postName, router))
    }

    // the use of `emptyRule` is just for nice formatting
    (emptyRule
      | home
      | fullPost)
      .notFound(redirectToPage(HomePageType)(Redirect.Replace))
  }

  def apply() = {
    Router(Config.baseUrl, routerConfig)()
  }
}

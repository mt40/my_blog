import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import pages.HomePage

object AppRouter {

  sealed trait PageRepr
  case object Home extends PageRepr

  private val baseUrl = {
    // Github Page serves "/<project_name>" not "/"
    val host = BaseUrl.fromWindowOrigin
    val base = if(host.value.contains("github")) Config.projectName else ""
    host / base
  }

  private val routerConfig = RouterConfigDsl[PageRepr].buildConfig { dsl =>
    import dsl._

    // the use of `emptyRule` is just for nice formatting
    (emptyRule
    // "/" -> home page
      | staticRoute(root, Home) ~> render(HomePage()))
      .notFound(redirectToPage(Home)(Redirect.Replace))
  }


  def apply() = {
    Router(baseUrl, routerConfig)()
  }
}

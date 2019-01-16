import common.Config
import japgolly.scalajs.react.extra.router.StaticDsl.Route
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import pages.{HomePage, _}

object AppRouter {

  /**
    * Contains all routes in this single pages app.
    *
    * Note that it is IMPORTANT that all routes except '/' (i.e. `root`)
    * have to start with a hash mark '#'. This is because only the part
    * before '#' will be sent to server. In our single page app, the server
    * only knows about ONE route that is '/'. It will only return the correct
    * 'index.html' & the JS file when it sees that route. When we have the
    * JS, we have the router which can handle the parts after '#'.
    *
    * @see https://stackoverflow.com/a/36623117/3778765
    */
  private val routerConfig = RouterConfigDsl[PageType].buildConfig { dsl =>
    import dsl._

    // "/" -> home page
    val home = staticRoute(root, HomePageType) ~> renderR(HomePage.apply)

    // "/#!post/<post_name>" -> full post page
    val fullPost = {
      val route: Route[FullPostPageType] = {
        val builder =
          ("#!post" / string("""[a-z0-9_\-]+""")) ~
            string("""#[a-z0-9_\-]+""").option
        builder.caseClass[FullPostPageType]
      }
      dynamicRouteCT(route) ~> dynRenderR { (page, router) =>
        FullPostPage(page.postId, page.anchor, router.urlFor(page).value)
      }
    }

    // /#!not_found
    val postNotFound = staticRoute("#!not_found", PostNotFoundPageType) ~> render(
      PostNotFoundPage()
    )

    // the use of `emptyRule` is just for nice formatting
    (emptyRule
      | home
      | fullPost
      | postNotFound)
      .notFound(redirectToPath("notfound.html")(Redirect.Replace))
      .setTitle(_.title)
  }

  def apply() = {
    Router(Config.baseUrl, routerConfig)()
  }
}

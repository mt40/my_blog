package common

import japgolly.scalajs.react.extra.router.BaseUrl

object Config {
  val projectName = "my_blog"

  val authorEmail = "minhthai40+blog@gmail.com"

  val domain = "undertherain.io"

  lazy val baseUrl: BaseUrl = {
    // Github Page serves "/<project_name>" not "/"
    val host = BaseUrl.fromWindowOrigin
    val base = if(host.value.contains("github")) Config.projectName else ""
    host / base
  }

  def isLive: Boolean = baseUrl.value contains domain

  /**
    * Number of similar posts to show.
    * Changing this may break the layout.
    */
  val similarPostsCount = 3
}

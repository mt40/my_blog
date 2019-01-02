package common

import japgolly.scalajs.react.extra.router.BaseUrl

object Config {
  val projectName = "my_blog"

  lazy val baseUrl: BaseUrl = {
    // Github Page serves "/<project_name>" not "/"
    val host = BaseUrl.fromWindowOrigin
    val base = if(host.value.contains("github")) Config.projectName else ""
    host / base
  }
}

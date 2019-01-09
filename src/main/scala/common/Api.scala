package common

import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl}

import scala.language.implicitConversions

object Api {
  private implicit def url2Absolute(url: BaseUrl): AbsUrl = url.abs

  def metadata: AbsUrl = Config.baseUrl / "posts/metadata.json"

  def post(id: String): AbsUrl = Config.baseUrl / "#" / "post" / id

  /** Returns an API to get resource given a url relative to the post folder. */
  def postResource(postId: String, resourceName: String): AbsUrl =
    Config.baseUrl / "posts" / postId / resourceName
}

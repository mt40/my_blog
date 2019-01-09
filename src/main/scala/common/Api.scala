package common

import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl}

import scala.language.implicitConversions

object Api {
  private implicit def url2Absolute(url: BaseUrl): AbsUrl = url.abs

  def metadata: AbsUrl = Config.baseUrl / "posts/metadata.json"

  def post(url: String): AbsUrl = Config.baseUrl / "posts" / url

  /** Returns an API to get resource given a url relative to the post folder. */
  def postResource(relativeUrl: String): AbsUrl =
    Config.baseUrl / "posts" / relativeUrl
}

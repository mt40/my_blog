package common

import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl}

import scala.language.implicitConversions

object Api {
  private implicit def url2Absolute(url: BaseUrl): AbsUrl = url.abs

  def metadata: AbsUrl = Config.baseUrl / "articles/metadata.json"

  def article(url: String): AbsUrl = Config.baseUrl / "articles" / url

  /** Returns an API to get resource given a url relative to the article folder. */
  def articleResource(relativeUrl: String): AbsUrl =
    Config.baseUrl / "articles" / relativeUrl
}

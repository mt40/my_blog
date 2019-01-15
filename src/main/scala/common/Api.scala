package common

import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl}

import scala.language.implicitConversions

object Api {
  private implicit def url2Absolute(url: BaseUrl): AbsUrl = url.abs

  def site: AbsUrl = Config.baseUrl

  def metadata: AbsUrl = Config.baseUrl / "posts/metadata.json"

  def post(id: String): AbsUrl = Config.baseUrl / "#!post" / id

  /** Returns an API to get resource given a url relative to the post folder. */
  def postResource(postId: String, resourceName: String): AbsUrl =
    Config.baseUrl / "posts" / postId / resourceName

  def imageAsset(imageName: String): AbsUrl =
    Config.baseUrl / "assets" / "images" / imageName

  /**
    * Unicode characters that make up the logo:
    * ▟██▙
    * ⋰⋰⋰
    */
  def siteLogo: AbsUrl = imageAsset("undertherain_logo_200.png")
}

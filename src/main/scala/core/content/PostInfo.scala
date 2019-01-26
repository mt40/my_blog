package core.content

import common.Date
import core.json.JsonParser
import upickle.default._

/**
  * Represents a post.
  *
  * @param id         unique id
  * @param title      post title
  * @param createDate when this post is created
  * @param tags       list of tags
  * @param file       name of file containing post content
  * @param image      name of thumbnail image file
  * @param summary    a short summary for this post
  */
case class PostInfo(
  id: String,
  title: String,
  createDate: Date,
  tags: Seq[String],
  file: String,
  image: Option[String] = None,
  summary: Option[String] = None
) {

  def sameAs(obj: Any): Boolean = {
    obj match {
      case e: PostInfo => id == e.id
      case _           => false
    }
  }
}

object PostInfo {
  lazy val empty: PostInfo = PostInfo(
    id = "",
    title = "",
    createDate = Date.zero,
    tags = Seq.empty,
    file = ""
  )

  implicit def OptionReader[T : Reader]: Reader[Option[T]] = JsonParser.option

  // 'wartremover' confuses macro with `var` :(
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  implicit val reader: Reader[PostInfo] = macroR
}

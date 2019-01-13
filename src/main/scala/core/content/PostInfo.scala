package core.content

import common.Date
import io.circe.Decoder

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

  /** For 'circe' to decode json. */
  implicit def decoder: Decoder[PostInfo] = {
    Decoder
      .forProduct7("id", "title", "createDate", "tags", "file", "image", "summary")(
        PostInfo.apply
      )
  }
}

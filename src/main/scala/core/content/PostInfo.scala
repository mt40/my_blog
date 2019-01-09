package core.content

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
  createDate: String,
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
    createDate = "",
    tags = Seq.empty,
    file = ""
  )
}

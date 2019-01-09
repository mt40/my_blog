package core.content

case class PostInfo(
  url: String,
  name: String,
  createDate: String,
  tags: Seq[String],
  image: Option[String] = None,
  summary: Option[String] = None
)

object PostInfo {
  lazy val empty: PostInfo = PostInfo("", "", "", Seq.empty)
}

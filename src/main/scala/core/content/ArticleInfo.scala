package core.content

case class ArticleInfo(
  url: String,
  name: String,
  createDate: String,
  tags: Seq[String],
  image: Option[String] = None,
  summary: Option[String] = None
)

object ArticleInfo {
  lazy val empty: ArticleInfo = ArticleInfo("", "", "", Seq.empty)
}

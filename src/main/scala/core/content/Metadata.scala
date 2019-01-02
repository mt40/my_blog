package core.content

case class Metadata(articles: Seq[ArticleInfo])

object Metadata {
  lazy val empty: Metadata = Metadata(Seq.empty)
}

package core.content

case class Article(info: ArticleInfo, markdown: String, html: String)

object Article {
  lazy val empty: Article = Article(ArticleInfo.empty, "", "")
}

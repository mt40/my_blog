package core.content

case class Metadata(posts: Seq[PostInfo])

object Metadata {
  lazy val empty: Metadata = Metadata(Seq.empty)
}

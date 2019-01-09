package core.content

case class Metadata(posts: Seq[PostInfo]) {

  def postsInDescDate: Seq[PostInfo] = {
    val descOrd = Ordering.fromLessThan[String](_ > _)
    posts.sortBy(_.createDate)(descOrd)
  }
}

object Metadata {
  lazy val empty: Metadata = Metadata(Seq.empty)
}

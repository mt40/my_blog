package core.content

import common.Date
import upickle.default._

case class Metadata(posts: Seq[PostInfo]) {

  def postsInDescDate: Seq[PostInfo] = {
    posts.sortBy(_.createDate)(Date.descOrdering)
  }
}

object Metadata {
  lazy val empty: Metadata = Metadata(Seq.empty)

  def apply(p: PostInfo): Metadata = Metadata(Seq(p))

  // 'wartremover' confuses macro with `var` :(
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  implicit val reader: Reader[Metadata] = macroR
}

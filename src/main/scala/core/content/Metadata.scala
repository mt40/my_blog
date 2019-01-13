package core.content

import common.Date
import io.circe.Decoder

case class Metadata(posts: Seq[PostInfo]) {

  def postsInDescDate: Seq[PostInfo] = {
    posts.sortBy(_.createDate)(Date.descOrdering)
  }
}

object Metadata {
  lazy val empty: Metadata = Metadata(Seq.empty)

  /** For 'circe' to decode json. */
  implicit def decoder(implicit info: Decoder[PostInfo]): Decoder[Metadata] = {
    Decoder.forProduct1[Metadata, Seq[PostInfo]]("posts")(Metadata(_))(Decoder.decodeSeq)
  }
}

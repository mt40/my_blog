package core.content

case class Post(info: PostInfo, markdown: String)

object Post {
  lazy val empty: Post = Post(PostInfo.empty, "")
}

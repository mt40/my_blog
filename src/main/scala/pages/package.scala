package object pages {

  /** Programmatic representation of a page. */
  sealed trait PageType {
    def title: String
  }

  case object HomePageType extends PageType {
    override def title: String = "Home"
  }

  case object NotFoundPageType extends PageType {
    override def title: String = "Not found"
  }

  case class FullPostPageType(postName: String) extends PageType {
    override def title: String = postName
  }
}

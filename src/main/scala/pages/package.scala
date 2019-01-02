package object pages {

  /** Programmatic representation of a page. */
  sealed trait PageType
  case object HomePageType                      extends PageType
  case class FullPostPageType(postName: String) extends PageType
}

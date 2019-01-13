package common

object Global {

  /**
    * Entry to jQuery selector. Although this is a powerful tool
    * to edit html, it breaks the flow of React and can create
    * a mess when used in too many places.
    * Therefore, think twice before using this.
    */
  lazy val jQuery = org.querki.jquery.$
}

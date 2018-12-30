import components._
import scalacss.DevDefaults._
import scalacss.internal.mutable.GlobalRegistry

object AppCSS {

  /** Call before using any React components to load CSS. */
  def load(): Unit = {
    GlobalRegistry.register(Title.Style)
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}

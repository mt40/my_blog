package components

import common.Api
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object NavBarComp {

  /** @param isBurgerActive if `true`, the hamburger menu will be opened */
  case class State(isBurgerActive: Boolean)

  object State {
    def default = State(false)
  }

  class Backend(scope: BackendScope[Unit, State]) {

    def onBurgerClick: Callback = {
      scope.modState(s => s.copy(isBurgerActive = !s.isBurgerActive))
    }

    def render(state: State): VdomNode = {
      val isBurgerActive = if(state.isBurgerActive) Some("is-active") else None

      {
        import japgolly.scalajs.react.vdom.all._

        val brand = {
          div(
            cls := "navbar-brand",
            a(
              cls := "navbar-item padding-left-0 no-bg",
              cls :=? isBurgerActive,
              href := Api.site.value,
              "My Blog"
            ),
            // the hamburger icon, which toggles the navbar menu on touch devices
            a(
              cls := "navbar-burger burger",
              span(),
              span(),
              span(),
              onClick --> onBurgerClick
            )
          )
        }

        val menu = {
          val menuItems = if(state.isBurgerActive) {
            TagMod(
              a(cls := "navbar-item padding-left-0", "About"),
              a(cls := "navbar-item padding-left-0", "Night Mode \uD83C\uDF12")
            )
          } else {
            a(cls := "navbar-item", "Night Mode \uD83C\uDF12")
          }

          div(
            cls := "navbar-menu no-shadow no-bg",
            cls :=? isBurgerActive,
            div(cls := "navbar-start"),
            div(cls := "navbar-end", menuItems)
          )
        }

        div(
          cls := "columns",
          div(
            cls := "column is-10 is-offset-1 is-full-mobile padding-bot-0",
            nav(cls := "navbar no-bg", brand, menu)
          )
        )
      }
    }
  }

  private val component = {
    ScalaComponent
      .builder[Unit]("NavBar")
      .initialState(State.default)
      .renderBackend[Backend]
      .build
  }

  def apply() = component()
}

package components

import common.Api
import common.Global.jQuery
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object NavBarComp {

  case class Props(reloadDisqus: Option[() => Callback] = None)

  /** @param isBurgerActive if `true`, the hamburger menu will be opened */
  case class State(isBurgerActive: Boolean)

  object State {
    def default = State(false)
  }

  class Backend(scope: BackendScope[Props, State]) {

    private val nightModeClassName = "night-mode"
    private val lightCodeHighlightStyle = "tomorrow"
    private val darkCodeHighlightStyle = "atom-one-dark"

    def onBurgerClick: Callback = {
      scope.modState(s => s.copy(isBurgerActive = !s.isBurgerActive))
    }

    @deprecated("night mode is now permanent", since = "1.0-preview2")
    def onNightModeClick(props: Props): Callback = Callback {
      // change class of 'body'
      jQuery("body").toggleClass(nightModeClassName)

      // change logo & code highlight style
      val logo = jQuery("img.site-logo")
      val highlightStyle = jQuery("#code-highlight-style")
      val href = highlightStyle.attr("href")
      if(href.exists(_ contains lightCodeHighlightStyle)) {
        logo.attr("src", Api.siteLogoDark.value)
        highlightStyle.attr(
          "href",
          href.get.replace(lightCodeHighlightStyle, darkCodeHighlightStyle)
        )
      }
      else {
        logo.attr("src", Api.siteLogo.value)
        highlightStyle.attr(
          "href",
          href.get.replace(darkCodeHighlightStyle, lightCodeHighlightStyle)
        )
      }

      // reload 'Disqus' so it can automatically change to dark theme
      if(props.reloadDisqus.isDefined) {
        props.reloadDisqus.foreach(_.apply().runNow())
      }
    }

    def render(props: Props, state: State): VdomNode = {
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
              img(cls := "site-logo", src := Api.siteLogoDark.value),
              code(cls := "site-brand is-size-7", "under_the_rain")
            ),
            // the hamburger icon, which toggles the navbar menu on touch devices
            a(
              cls := "navbar-burger",
              span(),
              span(),
              span(),
              onClick --> onBurgerClick
            )
          )
        }

        val menu = {
          val menuItems = {
            if(state.isBurgerActive) {
              TagMod(
                a(
                  cls := "navbar-item padding-left-0 is-size-7",
                  href := Api.about.value,
                  "About"
                )
              )
            }
            else {
              TagMod.empty
            }
          }

          div(
            cls := "navbar-menu no-shadow no-bg",
            cls :=? isBurgerActive,
            div(cls := "navbar-start"),
            div(cls := "navbar-end", menuItems)
          )
        }

        div(
          cls := "columns font-san-serif",
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
      .builder[Props]("NavBar")
      .initialState(State.default)
      .renderBackend[Backend]
      .build
  }

  def apply() = component(Props())

  def apply(reloadDisqus: () => Callback) =
    component(Props(Some(reloadDisqus)))
}

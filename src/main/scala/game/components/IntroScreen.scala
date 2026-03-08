package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

/** Welcome / intro screen. */
@react object IntroScreen {
  case class Props(playerCount: Int, onStart: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    div(className := "screen intro-screen fade-in")(
      div(className := "intro-content")(
        h1(className := "game-title")("ONE%"),
        h2(className := "game-subtitle")("THE GAME SHOW"),
        div(className := "separator"),
        div(className := "intro-message")(
          p(className := "host-text")("Welcome! I am your host, Toni!"),
          p(className := "contestant-count")(
            s"We have ${props.playerCount} contestants from all over the UK"
          )
        ),
        button(
          className := "btn btn-gold btn-large",
          onClick := (_ => props.onStart())
        )("START GAME")
      )
    )
  }
}

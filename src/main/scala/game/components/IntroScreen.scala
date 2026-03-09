package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

/** Welcome / intro screen. */
@react object IntroScreen {
  case class Props(playerCount: Int, onStart: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val (showHowTo, setShowHowTo) = useState(false)

    div(className := "screen intro-screen fade-in")(
      div(className := "intro-content")(
        img(
          src := "logos/one-percent-logo.png",
          alt := "ONE%",
          className := "intro-logo"
        ),
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
        )("START GAME"),
        button(
          className := "btn btn-how-to",
          onClick := (_ => setShowHowTo(true))
        )("HOW TO PLAY")
      ),

      if (showHowTo) {
        div(className := "overlay fade-in", onClick := (_ => setShowHowTo(false)))(
          div(
            className := "overlay-card",
            onClick := (e => e.stopPropagation())
          )(
            button(
              className := "overlay-close",
              onClick := (_ => setShowHowTo(false))
            )("\u00D7"),
            h3(className := "overlay-title")("How to Play"),
            div(className := "overlay-body")(
              div(className := "htp-rule")(
                span(className := "htp-num")("1"),
                p()("You compete against ", strong()(s"${props.playerCount - 1}"), " CPU contestants in a trivia showdown.")
              ),
              div(className := "htp-rule")(
                span(className := "htp-num")("2"),
                p()("Each round, everyone answers the ", strong()("same question"), ". Answer correctly to survive.")
              ),
              div(className := "htp-rule")(
                span(className := "htp-num")("3"),
                p()("Questions get ", strong()("harder"), " each round — difficulty rises from 5% to 99%.")
              ),
              div(className := "htp-rule")(
                span(className := "htp-num")("4"),
                p()("Wrong answer? You're ", strong()("eliminated"), ". But you can keep watching as a spectator.")
              ),
              div(className := "htp-rule")(
                span(className := "htp-num")("5"),
                p()("You get ", strong()("one free pass"), " — skip a question without being eliminated.")
              ),
              div(className := "htp-rule")(
                span(className := "htp-num")("6"),
                p()("Survive every round to become part of the ", strong()("ONE%"), " — the last ones standing!")
              )
            )
          )
        )
      } else {
        div()()
      }
    )
  }
}

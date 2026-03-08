package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import game._

/** Final choice screen — sole surviving player decides: take the money or play the 99% question. */
@react object FinalChoiceScreen {
  case class Props(gs: GameState, onTake: () => Unit, onPlay: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val gs = props.gs
    val isYou = !gs.userEliminated

    div(className := "screen final-choice-screen fade-in")(
      div(className := "mini-logo-bar")(
        img(src := "logos/one-percent-logo-notext.png", alt := "ONE%", className := "mini-logo")
      ),

      /* Progress bar */
      ProgressBar(ProgressBar.Props(gs.currentRound + 1)),

      /* Crowd grid */
      CrowdGrid(CrowdGrid.Props(gs.allPlayers, gs.remainingPlayers, gs.eliminatedPlayers)),

      div(className := "final-choice-content slide-up")(
        div(className := "trophy-icon",
          dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophyLarge)
        ),

        h1(className := "final-choice-title")("THE FINAL"),
        div(className := "separator"),

        if (isYou) {
          /* User is the sole survivor — give them the choice */
          div(className := "final-choice-box")(
            p(className := "final-choice-msg")(
              span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.sparkles)),
              span(" You are the last one standing!")
            ),
            p(className := "final-choice-desc")(
              "You can take the money now and walk away as the winner, or risk it all on the 99% question."
            ),
            div(className := "final-choice-buttons")(
              button(
                className := "btn btn-gold btn-large",
                onClick := (_ => props.onTake())
              )(
                span(className := "btn-icon-text")(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.shieldCheck)),
                  span(" TAKE THE MONEY")
                )
              ),
              button(
                className := "btn btn-orange btn-large",
                onClick := (_ => props.onPlay())
              )(
                span(className := "btn-icon-text")(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.alertTriangle)),
                  span(" PLAY THE FINAL")
                )
              )
            )
          )
        } else {
          /* User is spectating — the sole survivor is an NPC */
          val survivor = gs.remainingPlayers.head
          div(className := "final-choice-box")(
            p(className := "final-choice-msg")(
              span(s"${survivor.name} is the last one standing!")
            ),
            p(className := "final-choice-desc")(
              s"${survivor.name} decides to play the final 99% question\u2026"
            ),
            div(className := "final-choice-buttons")(
              button(
                className := "btn btn-gold btn-large",
                onClick := (_ => props.onPlay())
              )(
                span(className := "btn-icon-text")(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.chevronRight)),
                  span(" CONTINUE")
                )
              )
            )
          )
        }
      )
    )
  }
}

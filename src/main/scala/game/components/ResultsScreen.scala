package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import game._

/** Shows round results — correct answer, eliminated players, continue button. */
@react object ResultsScreen {
  case class Props(gs: GameState, onNext: () => Unit)

  /** Mini stickman SVG (head + torso only, no legs) for eliminated list. */
  private def miniStickmanSvg(headColor: String, bodyColor: String): String =
    s"""<svg viewBox="0 0 20 26" class="elim-avatar">
       |  <circle cx="10" cy="5.5" r="4.5" fill="$headColor"/>
       |  <rect x="5" y="11" width="10" height="13" rx="3" fill="$bodyColor"/>
       |</svg>""".stripMargin

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val gs       = props.gs
    val question = gs.currentQuestion.get
    val correctText   = GameLogic.getCorrectAnswer(question)
    val correctLetter = question.answer.toString

    val (resultClass, resultMsg) = if (gs.lastUserAnswer == "p") {
      ("result-pass", "\uD83C\uDFAB PASS USED \u2014 You advance!")
    } else if (gs.lastUserCorrect && !gs.userEliminated) {
      ("result-correct", "\u2713 CORRECT!")
    } else if (gs.lastUserAnswer == "timeout") {
      ("result-wrong", "\u23F0 TIME\u2019S UP \u2014 You are eliminated")
    } else if (gs.userEliminated && gs.lastUserAnswer.nonEmpty) {
      ("result-wrong", "\u2717 INCORRECT \u2014 You are eliminated")
    } else {
      ("result-watch", "Watching simulation\u2026")
    }

    val eliminatedCount   = gs.lastRoundEliminated.length
    val totalBeforeRound  = gs.remainingPlayers.length + eliminatedCount
    val eliminatedPercent = if (totalBeforeRound > 0) (eliminatedCount.toDouble / totalBeforeRound * 100).round.toInt else 0

    div(className := "screen results-screen fade-in")(
      /* Mini logo */
      div(className := "mini-logo-bar")(
        img(src := "logos/one-percent-logo-notext.png", alt := "ONE%", className := "mini-logo")
      ),

      /* Correct answer */
      h2(className := "gold-text")("CORRECT ANSWER"),
      div(className := "correct-answer-box")(
        p(s"($correctLetter) $correctText")
      ),
      p(className := s"result-message $resultClass")(resultMsg),
      div(className := "separator"),

      /* Crowd */
      CrowdGrid(CrowdGrid.Props(gs.allPlayers, gs.remainingPlayers, gs.eliminatedPlayers)),

      /* Elimination details */
      if (gs.isNullRound) {
        div(className := "null-round-box")(
          h3("\u26A0\uFE0F NULL ROUND!"),
          p("No one got the answer correct!"),
          p(className := "sub-text")("Let\u2019s try another question\u2026")
        )
      } else if (eliminatedCount > 0) {
        div(className := "elimination-section")(
          h3(className := "elim-heading")(s"\u274C $eliminatedCount PLAYERS ELIMINATED"),
          div(className := "elim-stats")(
            span(className := "elim-pct")(s"$eliminatedPercent%"),
            span("of players got it wrong")
          ),
          div(className := "elim-bar-bg")(
            div(className := "elim-bar-fill", style := js.Dynamic.literal("width" -> s"$eliminatedPercent%"))
          ),
          ul(className := "elim-list")(
            gs.lastRoundEliminated.map { p =>
              val bodyColor = RegionColors.get(p.region)
              val svgHtml   = miniStickmanSvg(p.skinTone, bodyColor)
              li(key := p.name, className := "elim-item")(
                div(className := "elim-avatar-wrap",
                  dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> svgHtml)
                ),
                div(className := "elim-info")(
                  span(className := "elim-name")(p.name),
                  span(className := "elim-location")(p.location)
                )
              )
            }
          )
        )
      } else {
        p(className := "no-elim")("\u2713 No players eliminated this round!")
      },

      /* Remaining count */
      if (!gs.isNullRound) {
        div(className := "remaining-box")(
          span(className := "remaining-count")(gs.remainingPlayers.length.toString),
          span(className := "remaining-label")("PLAYERS REMAINING")
        )
      } else div(),

      /* Continue */
      button(
        className := "btn btn-gold btn-large",
        onClick := (_ => props.onNext())
      )(
        if (gs.remainingPlayers.length <= 1) "\uD83C\uDFC6 SEE WINNER" else "NEXT ROUND \u25B6"
      )
    )
  }
}

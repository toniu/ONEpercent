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

    val (resultClass, resultIcon, resultMsg) = if (gs.lastUserAnswer == "p") {
      ("result-pass", SvgIcons.ticket, "PASS USED \u2014 You advance!")
    } else if (gs.lastUserCorrect && !gs.userEliminated) {
      ("result-correct", SvgIcons.checkCircle, "CORRECT!")
    } else if (gs.lastUserAnswer == "timeout") {
      ("result-wrong", SvgIcons.clock, "TIME\u2019S UP \u2014 You are eliminated")
    } else if (gs.userEliminated && gs.lastUserAnswer.nonEmpty) {
      ("result-wrong", SvgIcons.xCircle, "INCORRECT \u2014 You are eliminated")
    } else {
      ("result-watch", SvgIcons.eye, "Watching simulation\u2026")
    }

    val eliminatedCount   = gs.lastRoundEliminated.length
    val totalBeforeRound  = gs.remainingPlayers.length + eliminatedCount
    val eliminatedPercent = if (totalBeforeRound > 0) (eliminatedCount.toDouble / totalBeforeRound * 100).round.toInt else 0

    div(className := "screen results-screen fade-in")(
      /* Mini logo */
      div(className := "mini-logo-bar")(
        img(src := "logos/one-percent-logo-notext.png", alt := "ONE%", className := "mini-logo")
      ),

      /* Progress bar */
      ProgressBar(ProgressBar.Props(gs.currentRound)),

      /* Crowd grid */
      CrowdGrid(CrowdGrid.Props(gs.allPlayers, gs.remainingPlayers, gs.eliminatedPlayers)),

      /* Correct answer */
      h2(className := "gold-text")("CORRECT ANSWER"),
      div(className := "correct-answer-box")(
        p(s"($correctLetter) $correctText")
      ),
      div(className := s"result-message $resultClass")(
        span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> resultIcon)),
        span(s" $resultMsg")
      ),
      div(className := "separator"),

      /* Elimination details */
      if (gs.isNullRound) {
        div(className := "null-round-box")(
          h3(
            span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.alertTriangle)),
            span(" NULL ROUND!")
          ),
          p("No one got the answer correct!"),
          p(className := "sub-text")("Let\u2019s try another question\u2026")
        )
      } else if (eliminatedCount > 0) {
        val userAlsoOut = gs.userEliminated
        val sectionCls  = if (userAlsoOut) "elimination-section elim-user-out" else "elimination-section elim-user-safe"
        div(className := sectionCls)(
          h3(className := "elim-heading")(
            span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.xCircle)),
            span(s" $eliminatedCount PLAYERS ELIMINATED")
          ),
          div(className := "elim-bar-bg")(
            div(className := "elim-bar-fill", style := js.Dynamic.literal("width" -> s"$eliminatedPercent%"))
          ),
          ul(className := "elim-list")(
            gs.lastRoundEliminated.map { p =>
              val isYou     = p.name == "You"
              val headCol   = if (isYou) "#c3fc02" else p.skinTone
              val bodyCol   = if (isYou) "#c3fc02" else RegionColors.get(p.region)
              val svgHtml   = miniStickmanSvg(headCol, bodyCol)
              val itemCls   = if (isYou) "elim-item elim-item-you" else "elim-item"
              li(key := p.name, className := itemCls)(
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
        div(className := "no-elim")(
          span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.shieldCheck)),
          span(" No players eliminated this round!")
        )
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
        span(className := "btn-icon-text")({
          val lastDiffIdx = GameLogic.difficulties.length - 1
          val isLastRound = gs.currentRound - 1 >= lastDiffIdx
          val nextDiffIdx = math.min(gs.currentRound, lastDiffIdx)
          val isAboutToPlayFinal = nextDiffIdx == lastDiffIdx

          if (gs.remainingPlayers.isEmpty || isLastRound) {
            span(
              span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophy)),
              span(if (gs.remainingPlayers.length > 1) s" SEE ${gs.remainingPlayers.length} WINNERS"
                   else if (gs.remainingPlayers.isEmpty) " SEE RESULT"
                   else " SEE WINNER")
            )
          } else if (isAboutToPlayFinal) {
            span(
              span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophy)),
              span(" THE FINAL QUESTION")
            )
          } else {
            span(
              span("NEXT ROUND "),
              span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.chevronRight))
            )
          }
        })
      )
    )
  }
}

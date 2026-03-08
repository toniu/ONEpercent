package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import game._

/** Displays the current question with answer options, crowd grid, and pass button. */
@react object QuestionScreen {
  case class Props(gs: GameState, onAnswer: String => Unit, onAutoSimulate: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val gs = props.gs
    val question = gs.currentQuestion.get
    val canPass = !gs.userEliminated && !gs.userPassUsed &&
      gs.remainingPlayers.length > (gs.allPlayers.length * 0.25)

    /* Auto-simulate after delay when user is already eliminated */
    useEffect(() => {
      if (gs.userEliminated) {
        val timerId = scala.scalajs.js.timers.setTimeout(2000) {
          props.onAutoSimulate()
        }
        (() => scala.scalajs.js.timers.clearTimeout(timerId)): Unit
      }
    }, Seq(gs.userEliminated, gs.currentRound))

    div(className := "screen question-screen fade-in")(
      /* Progress bar */
      ProgressBar(ProgressBar.Props(gs.currentRound)),

      /* Header */
      div(className := "q-header")(
        div(className := "q-header-left")(
          h2(className := "round-label")(s"ROUND ${gs.currentRound}"),
          span(className := "category-badge")(
            span(className := "cat-emoji")(GameLogic.getCategoryEmoji(question.category)),
            span(GameLogic.getCategoryName(question.category).toUpperCase)
          )
        ),
        div(className := "q-header-center")(
          span(className := "players-count")(gs.remainingPlayers.length.toString),
          span(className := "players-label")("PLAYERS LEFT")
        ),
        div(className := "q-header-right")(
          span(className := "diff-label")("DIFFICULTY"),
          DifficultyDots(DifficultyDots.Props(question.difficulty))
        )
      ),

      /* Crowd grid */
      CrowdGrid(CrowdGrid.Props(gs.allPlayers, gs.remainingPlayers, gs.eliminatedPlayers)),

      /* Question box */
      div(className := "question-box scale-in")(
        p(className := "question-text")(question.prompt)
      ),

      /* Options */
      div(className := "options-container")(
        question.options.zipWithIndex.map { case (opt, idx) =>
          val letter = ('a' + idx).toChar.toString
          div(
            key := letter,
            className := s"option-btn stagger-$idx",
            onClick := (_ => if (!gs.userEliminated) props.onAnswer(letter))
          )(
            span(className := "option-letter")(letter.toUpperCase),
            span(className := "option-text")(opt)
          )
        }
      ),

      /* Pass button */
      if (canPass) {
        div(className := "pass-container")(
          button(
            className := "btn btn-orange",
            onClick := (_ => props.onAnswer("p"))
          )("\uD83C\uDFAB USE PASS")
        )
      } else if (gs.userEliminated) {
        div(className := "status-eliminated")(
          "\uD83D\uDC40 You are eliminated \u2014 watching simulation"
        )
      } else {
        div()
      }
    )
  }
}

/** Round progress bar showing difficulty tiers. */
@react object ProgressBar {
  case class Props(currentRound: Int)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    div(className := "progress-bar")(
      GameLogic.difficulties.zipWithIndex.map { case (diff, idx) =>
        val cls = if (idx < props.currentRound - 1) "prog-done"
                  else if (idx == props.currentRound - 1) "prog-active"
                  else "prog-future"
        div(key := idx.toString, className := s"prog-step $cls")(
          div(className := "prog-pip"),
          span(className := "prog-label")(diff.toString)
        )
      }
    )
  }
}

/** Difficulty dot indicator (1-5 dots). */
@react object DifficultyDots {
  case class Props(difficulty: Int)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val numDots = math.max(1, math.min(5, math.ceil(props.difficulty / 100.0 * 5).toInt))
    div(className := "diff-dots")(
      (1 to 5).map { i =>
        span(key := i.toString, className := (if (i <= numDots) "dot dot-active" else "dot"))
      }
    )
  }
}

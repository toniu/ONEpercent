package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import game._

/** Root application component — manages all game state. */
@react object App {
  type Props = Unit

  val component: FunctionalComponent[Unit] = FunctionalComponent[Unit] { _ =>
    val (state, setState) = useState[GameState] {
      val players   = GameData.loadPlayers()
      val questions = GameData.loadQuestions()
      val categories = GameData.loadCategories()
      val initial   = GameLogic.userPlayer +: players
      GameState(
        screen           = Screen.Intro,
        allPlayers       = initial,
        remainingPlayers = initial,
        questions        = questions,
        categories       = categories
      )
    }

    def startGame(): Unit = {
      val nextRound = 1
      val difficulty = GameLogic.difficulties(math.min(nextRound - 1, GameLogic.difficulties.length - 1))
      val question = GameLogic.selectQuestion(state.questions, difficulty)
      setState(state.copy(
        screen          = Screen.Question,
        currentRound    = nextRound,
        currentQuestion = Some(question)
      ))
    }

    def handleAnswer(answer: String): Unit = {
      val question = state.currentQuestion.get
      val correctIdx = question.options.indexOf(question.correctAnswer)
      val correctLetter = ('a' + correctIdx).toChar.toString
      val isCorrect = if (answer == "p") true
                      else answer == correctLetter

      val newUserEliminated = if (!state.userEliminated && answer != "p" && !isCorrect) true
                              else state.userEliminated

      val eliminated = GameLogic.simulateAnswers(question, state.remainingPlayers, newUserEliminated)
      val allEliminated = eliminated.length == state.remainingPlayers.length
      val lastDiffIdx = GameLogic.difficulties.length - 1
      val isFinalRound = state.currentRound - 1 >= lastDiffIdx

      // Null round only in non-final rounds — in the final, total wipeout means zero winners
      val isNullRound = allEliminated && !isFinalRound

      val (newRemaining, newEliminated) = if (isNullRound) {
        (state.remainingPlayers, state.eliminatedPlayers)
      } else {
        (
          state.remainingPlayers.filterNot(p => eliminated.contains(p)),
          state.eliminatedPlayers ++ eliminated
        )
      }

      setState(state.copy(
        screen              = Screen.Results,
        userEliminated      = newUserEliminated,
        userPassUsed        = if (answer == "p") true else state.userPassUsed,
        remainingPlayers    = newRemaining,
        eliminatedPlayers   = newEliminated,
        lastRoundEliminated = eliminated,
        isNullRound         = isNullRound,
        lastUserAnswer      = answer,
        lastUserCorrect     = isCorrect
      ))
    }

    def handleAutoSimulate(): Unit = {
      val question = state.currentQuestion.get
      val eliminated = GameLogic.simulateAnswers(question, state.remainingPlayers, state.userEliminated)
      val allEliminated = eliminated.length == state.remainingPlayers.length
      val lastDiffIdx = GameLogic.difficulties.length - 1
      val isFinalRound = state.currentRound - 1 >= lastDiffIdx
      val isNullRound = allEliminated && !isFinalRound

      val (newRemaining, newEliminated) = if (isNullRound) {
        (state.remainingPlayers, state.eliminatedPlayers)
      } else {
        (
          state.remainingPlayers.filterNot(p => eliminated.contains(p)),
          state.eliminatedPlayers ++ eliminated
        )
      }

      setState(state.copy(
        screen              = Screen.Results,
        remainingPlayers    = newRemaining,
        eliminatedPlayers   = newEliminated,
        lastRoundEliminated = eliminated,
        isNullRound         = isNullRound,
        lastUserAnswer      = "",
        lastUserCorrect     = false
      ))
    }

    def nextRound(): Unit = {
      val lastDiffIdx = GameLogic.difficulties.length - 1
      val isLastRound = state.currentRound - 1 >= lastDiffIdx

      if (state.remainingPlayers.isEmpty) {
        // Everyone eliminated — no winners
        setState(state.copy(screen = Screen.Winner, winners = List()))
      } else if (isLastRound) {
        // Just finished the 99% question — all survivors are winners
        setState(state.copy(screen = Screen.Winner, winners = state.remainingPlayers))
      } else {
        // If only 1 player remains, skip straight to the 99% final
        val nextRoundNum = if (state.isNullRound) state.currentRound else state.currentRound + 1
        val skipToFinal  = state.remainingPlayers.length == 1 && nextRoundNum - 1 < lastDiffIdx
        val finalRoundNum = if (skipToFinal) lastDiffIdx + 1 else nextRoundNum
        val nextDiffIdx  = math.min(finalRoundNum - 1, lastDiffIdx)
        val difficulty   = GameLogic.difficulties(nextDiffIdx)
        val question     = GameLogic.selectQuestion(state.questions, difficulty)
        setState(state.copy(
          screen          = Screen.Question,
          currentRound    = finalRoundNum,
          currentQuestion = Some(question)
        ))
      }
    }

    def resetGame(): Unit = {
      val players  = GameData.loadPlayers()
      val initial  = GameLogic.userPlayer +: players
      setState(GameState(
        screen           = Screen.Intro,
        allPlayers       = initial,
        remainingPlayers = initial,
        questions        = state.questions,
        categories       = state.categories
      ))
    }

    div(className := "app-container")(
      state.screen match {
        case Screen.Intro =>
          IntroScreen(IntroScreen.Props(
            playerCount = state.remainingPlayers.length,
            onStart     = () => startGame()
          ))
        case Screen.Question =>
          QuestionScreen(QuestionScreen.Props(
            gs              = state,
            onAnswer        = (a: String) => handleAnswer(a),
            onAutoSimulate  = () => handleAutoSimulate()
          ))
        case Screen.Results =>
          ResultsScreen(ResultsScreen.Props(
            gs        = state,
            onNext    = () => nextRound()
          ))
        case Screen.Winner =>
          WinnerScreen(WinnerScreen.Props(
            winners = state.winners,
            onReset = () => resetGame()
          ))
      }
    )
  }
}

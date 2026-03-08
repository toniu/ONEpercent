package game

/** Represents which screen the game is currently showing. */
sealed trait Screen
object Screen {
  case object Intro       extends Screen
  case object Question    extends Screen
  case object Results     extends Screen
  case object FinalChoice extends Screen
  case object Winner      extends Screen
}

/** Immutable snapshot of the entire game state. */
case class GameState(
  screen: Screen                     = Screen.Intro,
  allPlayers: List[Player]           = List(),
  remainingPlayers: List[Player]     = List(),
  eliminatedPlayers: List[Player]    = List(),
  questions: List[Question]          = List(),
  categories: List[Category]         = List(),
  currentRound: Int                  = 0,
  userPassUsed: Boolean              = false,
  userEliminated: Boolean            = false,
  currentQuestion: Option[Question]  = None,
  lastRoundEliminated: List[Player]  = List(),
  isNullRound: Boolean               = false,
  lastUserAnswer: String             = "",
  lastUserCorrect: Boolean           = false,
  winners: List[Player]              = List()
)

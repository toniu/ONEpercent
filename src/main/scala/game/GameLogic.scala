package game

import scala.util.Random

/** Pure game logic — no I/O, no UI, fully Scala.js compatible. */
object GameLogic {

  val difficulties: List[Int] = List(5, 10, 15, 25, 30, 45, 50, 60, 75, 80, 90, 99)

  val userPlayer: Player = Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0)

  def selectQuestion(questions: List[Question], difficulty: Int): Question = {
    val eligible = questions.filter(_.difficulty == difficulty)
    Random.shuffle(eligible).head
  }

  def simulateAnswers(question: Question, remainingPlayers: List[Player], userEliminated: Boolean): List[Player] = {
    remainingPlayers.filterNot { player =>
      if (player.name == "You") {
        !userEliminated
      } else {
        val categoryRating = getCategoryRating(player, question.category)
        val probability = categoryRating / 100.0
        val difficultyShift = probability + ((1 - (question.difficulty / 100.0)) * 0.18)
        val roundedDS = BigDecimal(difficultyShift)
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
          .toDouble
          .min(0.90)
        val randomiser = BigDecimal(Random.nextDouble())
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
          .toDouble
        !(randomiser > roundedDS)
      }
    }
  }

  def getCategoryRating(player: Player, category: String): Int = category match {
    case "GEN" => player.GEN
    case "SPO" => player.SPO
    case "MUS" => player.MUS
    case "MAT" => player.MAT
    case "LAN" => player.LAN
    case "TEC" => player.TEC
    case "GEO" => player.GEO
    case "HIS" => player.HIS
    case "REC" => player.REC
    case _     => 0
  }

  def getCorrectAnswer(question: Question): String = question.answer match {
    case 'a' => question.options(0)
    case 'b' => question.options(1)
    case 'c' => question.options(2)
    case 'd' => question.options(3)
    case _   => "Unknown"
  }

  def getCategoryName(code: String): String = code match {
    case "GEN" => "General Knowledge"
    case "SPO" => "Sports & Entertainment"
    case "MUS" => "Music & Arts"
    case "MAT" => "Mathematics & Geometry"
    case "LAN" => "Language & Literature"
    case "TEC" => "Technology & Science"
    case "GEO" => "Geography & Nature"
    case "HIS" => "History & Politics"
    case "REC" => "Religion & Culture"
    case _     => "Unknown"
  }

  def getCategoryEmoji(code: String): String = code match {
    case "GEN" => "\uD83E\uDDE0"
    case "SPO" => "\u26BD"
    case "MUS" => "\uD83C\uDFB5"
    case "MAT" => "\uD83D\uDD22"
    case "LAN" => "\uD83D\uDCDA"
    case "TEC" => "\uD83D\uDCBB"
    case "GEO" => "\uD83C\uDF0D"
    case "HIS" => "\uD83C\uDFDB\uFE0F"
    case "REC" => "\uD83D\uDD4C"
    case _     => "\u2753"
  }
}

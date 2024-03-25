package game

import scala.util.Random
import scala.io.StdIn
import game.Player
import game.Question
import game.Category

object Game {
  val PlayersFile = "src/main/scala/game/csv/players.csv"
  val QuestionsFile = "src/main/scala/game/csv/questions.csv"
  val CategoriesFile = "src/main/scala/game/csv/categories.csv"

  def main(args: Array[String]): Unit = {
    val players = DataLoader.loadPlayers(PlayersFile)
    val questions = DataLoader.loadQuestions(QuestionsFile)
    val categories = DataLoader.loadCategories(CategoriesFile)

    playGame(players, questions, categories)
  }

  def playGame(players: List[Player], questions: List[Question], categories: List[Category]): Unit = {
    val difficulties = List(5, 10, 15, 25, 30, 45, 50, 60, 75, 80, 90, 99)
    var currentRound = 0
    var remainingPlayers = players
    var userPassUsed = false
    var userEliminated = false

    while (remainingPlayers.length > 1) {
      currentRound += 1
      val currentDifficulty = difficulties(math.min(currentRound - 1, difficulties.length - 1))
      val selectedQuestion = selectQuestion(questions, currentDifficulty)
      presentQuestion(currentRound, selectedQuestion, remainingPlayers.length, userPassUsed)

      if (remainingPlayers.length <= 25) {
        println("You cannot use your pass for this round - we are down to less than the final 25% of players")
        userPassUsed = true
      }

      if (!userEliminated) {
        val userAnswer = getUserAnswer(selectedQuestion)
        if (userAnswer != "p") {
          if (userAnswer == selectedQuestion.answer) {
            println("Correct answer!")
          } else {
            println("Incorrect answer! You are eliminated.")
            userEliminated = true
          }
        } else {
          if (!userPassUsed && remainingPlayers.length > 25) {
            println("Pass used. You'll be passed to the next round.")
            userPassUsed = true
          } else if (userPassUsed) {
            println("Pass mark is already used")
          } else {
            println("Cannot use pass at this stage of the game")
          }
        }
      }

      val eliminatedPlayers = simulateAnswers(selectedQuestion, remainingPlayers)
      remainingPlayers = remainingPlayers.filterNot(p => eliminatedPlayers.contains(p))

      println("\nCorrect answer: " + selectedQuestion.answer)
      println("\nPlayers Eliminated (" + eliminatedPlayers.length + " players ELIMINATED):" + eliminatedPlayers.map(_.name).mkString(", "))
      println("\nRemaining players (" + remainingPlayers.length + " players left):" + remainingPlayers.map(_.name).mkString(", "))

      if (userEliminated) {
        println("\nPress Enter to continue to the next round...")
        StdIn.readLine() // Wait for user input to continue to the next round
      }
    }

    println("\nThe winner is: " + remainingPlayers.head.name + " from " + remainingPlayers.head.location + ", UK")
  }

  def selectQuestion(questions: List[Question], difficulty: Int): Question = {
    val eligibleQuestions = questions.filter(_.difficulty == difficulty)
    Random.shuffle(eligibleQuestions).head
  }

  def presentQuestion(round: Int, question: Question, remainingPlayers: Int, userPassUsed: Boolean): Unit = {
    println(s"\nRound $round (Category: ${question.category}):")
    println(s"Only ${100 - question.difficulty}% of people can answer this question.")
    println(s"${question.prompt}")
    println("Options:")
    question.options.zipWithIndex.foreach { case (option, index) =>
      println(s"${('a' + index).toChar}. $option")
    }
    if (!userPassUsed && remainingPlayers > 25) {
      println("Enter your answer (a, b, c, d) or 'p' to use pass:")
    } else {
      println("Enter your answer (a, b, c, d):")
    }
  }

  def getUserAnswer(question: Question): String = {
    val userInput = StdIn.readLine().toLowerCase()
    if (userInput == "a" || userInput == "b" || userInput == "c" || userInput == "d" || userInput == "p") {
      userInput
    } else {
      println("Invalid input! Must be a letter a-d, or p")
      getUserAnswer(question)
    }
  }

  def simulateAnswers(question: Question, remainingPlayers: List[Player]): List[Player] = {
    remainingPlayers.filterNot { player =>
      if (player == Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0)) {
        false
      } else {
        val categoryRating = getCategoryRating(player, question.category)
        val probability = categoryRating / 100.0
        Random.nextDouble() > probability
      }
    }
  }

  def getCategoryRating(player: Player, category: String): Int = {
    category match {
      case "GEN" => player.GEN
      case "SPO" => player.SPO
      case "MUS" => player.MUS
      case "MAT" => player.MAT
      case "LAN" => player.LAN
      case "TEC" => player.TEC
      case "GEO" => player.GEO
      case "HIS" => player.HIS
      case "REC" => player.REC
      case _ => 0 // Default rating if category not found
    }
  }
}

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

    /* Add user to players list */
    val initialPlayers = Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0) +: players

    playGame(initialPlayers, questions, categories)
  }

  def playGame(players: List[Player], questions: List[Question], categories: List[Category]): Unit = {
    val difficulties = List(5, 10, 15, 25, 30, 45, 50, 60, 75, 80, 90, 99)
    var currentRound = 0
    var remainingPlayers = players
    var userPassUsed = false
    var userEliminated = false

    /* Introduce the game */
    introGame(remainingPlayers)

    /* Main game loop */
    while (remainingPlayers.length > 1) {
      currentRound += 1
      val currentDifficulty = difficulties(math.min(currentRound - 1, difficulties.length - 1))
      val selectedQuestion = selectQuestion(questions, currentDifficulty)
      presentQuestion(currentRound, selectedQuestion, remainingPlayers.length, userPassUsed, userEliminated)

      if (remainingPlayers.length <= (players.length * 0.25)) {
        println("You cannot use your pass for this round - we are down to less than the final 25% of players")
        userPassUsed = true
      }

      if (!userEliminated) {
        val userAnswer = getUserAnswer(selectedQuestion)
        if (userAnswer != "p") {
          if (userAnswer == selectedQuestion.answer.toString()) {
            println("Correct answer!")
          } else {
            println("Incorrect answer! You are eliminated.")
            userEliminated = true
          }
        } else {
          if (!userPassUsed && remainingPlayers.length > (players.length * 0.25)) {
            println("Pass used. You'll be passed to the next round.")
            userPassUsed = true
          } else if (userPassUsed) {
            println("Pass mark is already used")
          } else {
            println("Cannot use pass at this stage of the game")
          }
        }
      }

      println(s"\nCorrect answer: (${selectedQuestion.answer.toString()}) ${getCorrectAnswer(selectedQuestion)}")

      /* Simulate answers for CPU players */
      val eliminatedPlayers = simulateAnswers(selectedQuestion, remainingPlayers, userEliminated)

      if (eliminatedPlayers.length == remainingPlayers.length) {
        /* No one got the answer correct */
        println("\nNull round! No one got the answer correct! Let's keep going...")
        println("Press Enter to continue...")
        StdIn.readLine() // Wait for user input to continue to the next round
      } else {
        /* There are players who got the answer correct */
        remainingPlayers = remainingPlayers.filterNot(p => eliminatedPlayers.contains(p))

        if (eliminatedPlayers.length > 0) {
          println("\n- Players Eliminated (" + eliminatedPlayers.length + " eliminated): " + eliminatedPlayers.map(_.name).mkString(", "))
        } else {
          println("\nNo players eliminated this round! Let's keep going...")
        }

        if (remainingPlayers.length == 1) {
          println("\n---------- The game has found the ONE%!")
        } else {
          println("\n+ Remaining players (" + remainingPlayers.length + " left): " + remainingPlayers.map(_.name).mkString(", "))
        }

        if (userEliminated) {
          println("\nPress Enter to continue...")
          StdIn.readLine() // Wait for user input to continue to the next round
        }
      }  
    }

    println("\n---------- The winner is: " + remainingPlayers.head.name + " from " + remainingPlayers.head.location + ", UK\n")
  }

  def introGame(initialPlayers: List[Player]) = {
    println("\n---------- Welcome to ONE%, I am your host, Toni!")
    println(s"We have ${initialPlayers.length} contestants for this game from all over the UK...")

    println("\n" + initialPlayers.map(_.name).mkString(", "))

    println("\n---------- Press Enter to start the game...")
    StdIn.readLine() // Wait for user input to continue to the next round
  }

  def selectQuestion(questions: List[Question], difficulty: Int): Question = {
    val eligibleQuestions = questions.filter(_.difficulty == difficulty)
    Random.shuffle(eligibleQuestions).head
  }

  def presentQuestion(round: Int, question: Question, remainingPlayers: Int, userPassUsed: Boolean, userEliminated: Boolean): Unit = {
    println(s"\n---------- Round $round (Category: ${getCategoryName(question.category)}):")
    println(s"\n${question.prompt}")
    question.options.zipWithIndex.foreach { case (option, index) =>
      println(s"(${('a' + index).toChar}) $option")
    }
    if (userEliminated) {
      println("\nPress Enter to reveal answer...")
      StdIn.readLine() // Wait for user input to continue to the next round
    } else {
      if (!userPassUsed && remainingPlayers > 25) {
        println("\nEnter your answer (a, b, c, d) or 'p' to use pass:")
      } else {
        println("\nEnter your answer (a, b, c, d):")
      }
    }
  }

  def getUserAnswer(question: Question): String = {
    val userInput = StdIn.readLine().toLowerCase()
    if (userInput == "a" || userInput == "b" || userInput == "c" || userInput == "d" || userInput == "p") {
      userInput
    } else {
      println("Invalid input! Must be a letter a-d (answer options), or p (pass-round if available)")
      getUserAnswer(question)
    }
  }

  def simulateAnswers(question: Question, remainingPlayers: List[Player], userEliminated: Boolean): List[Player] = {
    println("\n...Debugging simulated answers: \n")
    remainingPlayers.filterNot { player =>
      if (player == Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0)) {
        !(userEliminated)
      } else {
        val categoryRating = getCategoryRating(player, question.category)
        val probability = categoryRating / 100.0
        /* The factors of the player's outcome depend on their ability on the category and the difficulty of the question */
        val difficultyShift = probability + ((1 - (question.difficulty / 100)) * 0.18)
        val roundedDS = BigDecimal(difficultyShift)
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
          .toDouble
          .min(0.90)
        val randomiser = BigDecimal(Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

        /* Eliminate based on randomiser */
        val playerEliminated = (randomiser > roundedDS)

        /* Debug message: temporary */
        var msg = ""
        var symbol = "+"
        if (playerEliminated) {
          msg = "--> ELIMINATED"
          symbol = "-"
        }
        println(s"${symbol} ${player.name} has ${question.category} probability shift ${roundedDS} against randomiser: ${randomiser}. ${msg}")

        !(playerEliminated)
      }
    }
  }

  def simulateAnswersGUI(question: Question, remainingPlayers: List[Player], userEliminated: Boolean): List[Player] = {
    remainingPlayers.filterNot { player =>
      if (player == Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0)) {
        !(userEliminated)
      } else {
        val categoryRating = getCategoryRating(player, question.category)
        val probability = categoryRating / 100.0
        /* The factors of the player's outcome depend on their ability on the category and the difficulty of the question */
        val difficultyShift = probability + ((1 - (question.difficulty / 100)) * 0.18)
        val roundedDS = BigDecimal(difficultyShift)
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
          .toDouble
          .min(0.90)
        val randomiser = BigDecimal(Random.nextDouble()).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

        /* Eliminate based on randomiser */
        val playerEliminated = (randomiser > roundedDS)

        !(playerEliminated)
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

  def getCorrectAnswer(question: Question): String = {
    question.answer.toString() match {
      case "a" => question.options(0)
      case "b" => question.options(1)
      case "c" => question.options(2)
      case "d" => question.options(3)
      case _ => throw new IllegalArgumentException("Invalid option letter. Use 'a', 'b', 'c', or 'd'.")
    }
  }

  def getCategoryName(categoryCode: String): String = {
    categoryCode match {
      case "GEN" => "General Knowledge"
      case "SPO" => "Sports & Entertainment"
      case "MUS" => "Music & Arts"
      case "MAT" => "Mathematics & Geometry"
      case "LAN" => "Language & Literature"
      case "TEC" => "Technology & Science"
      case "GEO" => "Geography & Nature"
      case "HIS" => "History & Politics"
      case "REC" => "Religion & Culture"
      case _ => "Unknown" // Default category name if code is not recognized
    }
  }

}

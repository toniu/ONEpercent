package game

import scala.util.Random

/** Parses CSV data (auto-embedded at compile time from csv/ files) and provides game data. */
object GameData {

  def loadPlayers(): List[Player] = {
    val lines = CsvData.playersCSV.trim.split("\n").toList.tail
    val shuffled = Random.shuffle(lines)
    shuffled.take(99).map { line =>
      val parts = line.split(",")
      Player(parts(0), parts(1), parts(2).toInt, parts(3).toInt, parts(4).toInt,
        parts(5).toInt, parts(6).toInt, parts(7).toInt, parts(8).toInt, parts(9).toInt, parts(10).toInt,
        parts(11), parts(12), parts(13))
    }
  }

  def loadQuestions(): List[Question] = {
    val lines = CsvData.questionsCSV.trim.split("\n").toList.tail
    lines.flatMap { line =>
      val parts = line.split(",")
      if (parts.length >= 5) {
        val options = parts(1).replaceAll("^\"|\"$", "").split(";").toList
        val questionText = parts(0).replaceAll("^\"|\"$", "")
        Some(Question(questionText, options, parts(2).trim.head, parts(3).trim, parts(4).trim.toInt))
      } else None
    }
  }

  def loadCategories(): List[Category] = {
    val lines = CsvData.categoriesCSV.trim.split("\n").toList.tail
    lines.map { line =>
      val parts = line.split(",")
      Category(parts(0), parts(1))
    }
  }
}


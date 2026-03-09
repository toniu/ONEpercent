package game

case class Question(prompt: String, options: List[String], correctAnswer: String, category: String, difficulty: Int)
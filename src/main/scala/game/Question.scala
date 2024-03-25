package game

case class Question(prompt: String, options: List[String], answer: Char, category: String, difficulty: Int)
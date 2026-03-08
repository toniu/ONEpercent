package game

import scala.io.Source
import scala.util.Random
import game.Player
import game.Question
import game.Category

object DataLoader {
    def loadPlayers(filename: String): List[Player] = {
        val players = Source.fromFile(filename).getLines().toList.tail // Skip the first line
        val shuffledPlayers = Random.shuffle(players)
        val selectedPlayers = shuffledPlayers.take(99) // Take the first 99 players
        /* The player's attributes:
        Name of player
        The location they are from (based in UK)
        Their rating in General knowledge
        Their rating in Sports & Entertainment
        Their rating in Music & Arts
        Their rating in Mathematics & Geometry
        Their rating in Language & Literature
        Their rating in Technology & Science
        Their rating in Geography & Nature
        Their rating in History & Politics
        Their rating in Religion & Culture
        */
        selectedPlayers.map { line =>
            val Array(name, location, strGEN, strSPO, strMUS, strMAT, strLAN, strTEC, strGEO, strHIS, strREC) = line.split(",")
            Player(name, location, strGEN.toInt, strSPO.toInt, strMUS.toInt, strMAT.toInt, strLAN.toInt, strTEC.toInt, strGEO.toInt, strHIS.toInt, strREC.toInt )
        }
    }   
    def loadQuestions(filename: String): List[Question] = {
        val questions = Source.fromFile(filename).getLines().toList.tail // Skip the first line
        questions.flatMap { line =>
            val parts = line.split(",")
            if (parts.length == 5) {
                val Array(prompt, optionsStr, answer, category, difficultyStr) = parts
                val options = optionsStr.split(";").toList
                Some(Question(prompt, options, answer.head, category, difficultyStr.toInt))
            } else {
                /* Skip line with invalid format */
                None
            }
        }
    }

    def loadCategories(filename: String): List[Category] = {
        val categories = Source.fromFile(filename).getLines().toList.tail // Skip the first line
        categories.map { line => 
            val Array(code,name) = line.split(",")
            Category(code,name)
        }
     }
}
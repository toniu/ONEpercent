package game

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.{Color, LinearGradient, Stop, CycleMethod}
import scalafx.scene.shape.{Rectangle, Circle}
import scalafx.scene.effect.DropShadow
import scalafx.application.Platform
import scalafx.animation.{FadeTransition, ScaleTransition, ParallelTransition, SequentialTransition, Timeline, KeyFrame, KeyValue}
import scalafx.util.Duration
import scalafx.Includes._
import scala.util.Random

object GameGUI extends JFXApp3 {
  val PlayersFile = "src/main/scala/game/csv/players.csv"
  val QuestionsFile = "src/main/scala/game/csv/questions.csv"
  val CategoriesFile = "src/main/scala/game/csv/categories.csv"

  var players: List[Player] = List()
  var questions: List[Question] = List()
  var categories: List[Category] = List()
  var remainingPlayers: List[Player] = List()
  var allPlayers: List[Player] = List()
  var eliminatedPlayers: List[Player] = List()
  var currentRound: Int = 0
  var userPassUsed: Boolean = false
  var userEliminated: Boolean = false
  val difficulties = List(5, 10, 15, 25, 30, 45, 50, 60, 75, 80, 90, 99)
  
  // Colors
  val primaryGold = Color.rgb(255, 215, 0)
  val darkBlue = Color.rgb(10, 15, 35)
  val lightBlue = Color.rgb(100, 150, 255)
  val eliminatedBlue = Color.rgb(30, 50, 120)
  val activeGlow = Color.rgb(255, 255, 255)
  val correctGreen = Color.rgb(50, 205, 50)
  val wrongRed = Color.rgb(220, 20, 60)

  override def start(): Unit = {
    // Load game data
    players = DataLoader.loadPlayers(PlayersFile)
    questions = DataLoader.loadQuestions(QuestionsFile)
    categories = DataLoader.loadCategories(CategoriesFile)
    
    // Add user to players list
    val initialPlayers = Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0) +: players
    remainingPlayers = initialPlayers
    allPlayers = initialPlayers
    eliminatedPlayers = List()

    stage = new JFXApp3.PrimaryStage {
      title = "ONE% - The Game Show"
      width = 1000
      height = 800
      scene = createIntroScreen()
    }
  }
  
  // Create crowd visualization with rows and columns
  def createCrowdVisual(showCrowd: Boolean = true): GridPane = {
    val grid = new GridPane {
      alignment = Pos.Center
      hgap = 6
      vgap = 6
      padding = Insets(10)
      style = "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10;"
      maxWidth = 700
      maxHeight = if (showCrowd) 200 else 0
      opacity = if (showCrowd) 1.0 else 0.0
    }
    
    val cols = 20
    val rows = 5
    val totalSpots = cols * rows
    
    for (i <- 0 until totalSpots) {
      val row = i / cols
      val col = i % cols
      
      val playerOpt = if (i < allPlayers.length) Some(allPlayers(i)) else None
      
      val isEliminated = playerOpt.exists(p => eliminatedPlayers.contains(p))
      val isActive = playerOpt.exists(p => remainingPlayers.contains(p))
      val isYou = playerOpt.exists(_.name == "You")
      
      val circle = new Circle {
        radius = 8
        fill = if (playerOpt.isEmpty) {
          Color.rgb(30, 30, 50, 0.3)
        } else if (isEliminated) {
          eliminatedBlue
        } else if (isYou) {
          primaryGold
        } else {
          activeGlow
        }
        
        stroke = if (isYou && isActive) primaryGold else Color.Transparent
        strokeWidth = if (isYou && isActive) 2 else 0
        
        effect = if (isActive && !isEliminated) {
          new DropShadow {
            radius = 10
            color = if (isYou) primaryGold else lightBlue
          }
        } else null
      }
      
      // Add pulsing animation for active players
      if (isActive && !isEliminated) {
        val pulse = new Timeline {
          cycleCount = Timeline.Indefinite
          autoReverse = true
          keyFrames = Seq(
            KeyFrame(Duration(0), values = Set(KeyValue(circle.opacity, 1.0))),
            KeyFrame(Duration(1000), values = Set(KeyValue(circle.opacity, 0.6)))
          )
        }
        pulse.play()
      }
      
      val tooltip = new Tooltip(playerOpt.map(_.name).getOrElse("Empty"))
      Tooltip.install(circle, tooltip)
      
      grid.add(circle, col, row)
    }
    
    grid
  }
  
  // Create round progress bar
  def createProgressBar(): HBox = {
    new HBox {
      alignment = Pos.Center
      spacing = 4
      padding = Insets(10, 0, 10, 0)
      children = difficulties.zipWithIndex.map { case (diff, index) =>
        new VBox {
          alignment = Pos.Center
          spacing = 4
          children = Seq(
            new Rectangle {
              width = 50
              height = 8
              fill = if (index < currentRound) primaryGold 
                    else if (index == currentRound) lightBlue
                    else Color.rgb(60, 60, 90, 0.5)
              arcWidth = 4
              arcHeight = 4
            },
            new Text {
              text = diff.toString
              font = Font.font("Arial", FontWeight.Bold, 10)
              fill = if (index < currentRound) primaryGold
                    else if (index == currentRound) Color.White
                    else Color.rgb(120, 120, 150)
            }
          )
        }
      }
    }
  }
  
  // Create category badge
  def createCategoryBadge(categoryCode: String): HBox = {
    val categoryName = Game.getCategoryName(categoryCode)
    val emoji = categoryCode match {
      case "GEN" => "🧠"
      case "SPO" => "⚽"
      case "MUS" => "🎵"
      case "MAT" => "🔢"
      case "LAN" => "📚"
      case "TEC" => "💻"
      case "GEO" => "🌍"
      case "HIS" => "🏛️"
      case "REC" => "🕌"
      case _ => "❓"
    }
    
    new HBox {
      alignment = Pos.Center
      spacing = 8
      padding = Insets(8, 20, 8, 20)
      style = s"""
        -fx-background-color: linear-gradient(to right, rgba(100, 150, 255, 0.3), rgba(100, 100, 200, 0.4));
        -fx-background-radius: 20;
        -fx-border-color: ${rgbToHex(lightBlue)};
        -fx-border-width: 2;
        -fx-border-radius: 20;
      """
      children = Seq(
        new Text {
          text = emoji
          font = Font.font("Arial", 20)
        },
        new Text {
          text = categoryName.toUpperCase
          font = Font.font("Arial", FontWeight.Bold, 14)
          fill = Color.White
        }
      )
    }
  }
  
  // Create difficulty indicator
  def createDifficultyIndicator(difficulty: Int): HBox = {
    val numDots = (difficulty / 100.0 * 5).ceil.toInt.max(1).min(5)
    new HBox {
      alignment = Pos.Center
      spacing = 3
      children = (1 to 5).map { i =>
        new Circle {
          radius = 4
          fill = if (i <= numDots) primaryGold else Color.rgb(80, 80, 120)
        }
      }
    }
  }

  def createIntroScreen(): Scene = {
    val scene = new Scene {
      fill = LinearGradient(
        startX = 0, startY = 0, endX = 0, endY = 1,
        proportional = true,
        cycleMethod = CycleMethod.NoCycle,
        stops = List(
          Stop(0, darkBlue),
          Stop(1, Color.rgb(20, 30, 60))
        )
      )
      
      content = new VBox {
        alignment = Pos.Center
        spacing = 25
        padding = Insets(40)
        
        children = Seq(
          new Text {
            text = "ONE%"
            font = Font.font("Arial", FontWeight.Bold, 80)
            fill = primaryGold
            style = "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 8, 0.3, 0, 0);"
          },
          new Text {
            text = "THE GAME SHOW"
            font = Font.font("Arial", FontWeight.Bold, 28)
            fill = Color.White
          },
          new Separator {
            maxWidth = 500
            style = "-fx-background-color: gold;"
          },
          createCrowdVisual(true),
          new VBox {
            alignment = Pos.Center
            spacing = 10
            children = Seq(
              new Text {
                text = s"Welcome! I am your host, Toni!"
                font = Font.font("Arial", FontWeight.Bold, 22)
                fill = Color.White
              },
              new Text {
                text = s"We have ${remainingPlayers.length} contestants from all over the UK"
                font = Font.font("Arial", 18)
                fill = lightBlue
              }
            )
          },
          new Button {
            text = "START GAME"
            font = Font.font("Arial", FontWeight.Bold, 22)
            style = s"""
              -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
              -fx-text-fill: ${rgbToHex(darkBlue)};
              -fx-padding: 18 50;
              -fx-background-radius: 10;
              -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.3), 8, 0.3, 0, 0);
            """
            onMouseEntered = e => {
              style = s"""
                -fx-background-color: linear-gradient(to bottom, #FFDD00, ${rgbToHex(primaryGold)});
                -fx-text-fill: ${rgbToHex(darkBlue)};
                -fx-padding: 18 50;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.5), 10, 0.4, 0, 0);
                -fx-scale-x: 1.05;
                -fx-scale-y: 1.05;
              """
            }
            onMouseExited = e => {
              style = s"""
                -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
                -fx-text-fill: ${rgbToHex(darkBlue)};
                -fx-padding: 18 50;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.3), 8, 0.3, 0, 0);
              """
            }
            onAction = _ => {
              currentRound = 0
              transitionToScene(createQuestionScreen())
            }
          }
        )
      }
    }
    
    // Fade in animation
    val rootNode = scene.root.value
    rootNode.setOpacity(0)
    val fadeIn = new FadeTransition(Duration(800), rootNode) {
      fromValue = 0
      toValue = 1
    }
    fadeIn.play()
    
    scene
  }
  
  // Helper for color conversions
  def rgbToHex(color: Color): String = {
    f"#${(color.red * 255).toInt}%02X${(color.green * 255).toInt}%02X${(color.blue * 255).toInt}%02X"
  }
  
  // Smooth transition between scenes
  def transitionToScene(newScene: Scene): Unit = {
    val currentRoot = stage.scene.value.root.value
    val fadeOut = new FadeTransition(Duration(300), currentRoot) {
      fromValue = 1
      toValue = 0
      onFinished = _ => {
        stage.scene = newScene
        val newRoot = newScene.root.value
        newRoot.setOpacity(0)
        val fadeIn = new FadeTransition(Duration(300), newRoot) {
          fromValue = 0
          toValue = 1
        }
        fadeIn.play()
      }
    }
    fadeOut.play()
  }

  def createQuestionScreen(): Scene = {
    currentRound += 1
    val currentDifficulty = difficulties(math.min(currentRound - 1, difficulties.length - 1))
    val selectedQuestion = selectQuestion(questions, currentDifficulty)
    
    val scene = new Scene {
      fill = LinearGradient(
        startX = 0, startY = 0, endX = 0, endY = 1,
        proportional = true,
        cycleMethod = CycleMethod.NoCycle,
        stops = List(
          Stop(0, darkBlue),
          Stop(1, Color.rgb(20, 30, 60))
        )
      )
      
      content = new VBox {
        alignment = Pos.TopCenter
        spacing = 15
        padding = Insets(30, 20, 30, 20)
        
        // Progress bar
        val progressBar = createProgressBar()
        
        // Header with round and player info
        val headerBox = new HBox {
          alignment = Pos.Center
          spacing = 60
          padding = Insets(10)
          style = "-fx-background-color: rgba(0, 0, 0, 0.4); -fx-background-radius: 15;"
          children = Seq(
            new VBox {
              alignment = Pos.Center
              spacing = 5
              children = Seq(
                new Text {
                  text = s"ROUND $currentRound"
                  font = Font.font("Arial", FontWeight.Bold, 28)
                  fill = primaryGold
                  effect = new DropShadow {
                    color = Color.rgb(255, 215, 0, 0.4)
                    radius = 8
                  }
                },
                createCategoryBadge(selectedQuestion.category)
              )
            },
            new VBox {
              alignment = Pos.Center
              spacing = 5
              children = Seq(
                new Text {
                  text = s"${remainingPlayers.length}"
                  font = Font.font("Arial", FontWeight.Bold, 36)
                  fill = Color.White
                  effect = new DropShadow {
                    color = Color.rgb(255, 255, 255, 0.3)
                    radius = 6
                  }
                },
                new Text {
                  text = "PLAYERS LEFT"
                  font = Font.font("Arial", FontWeight.Bold, 14)
                  fill = lightBlue
                }
              )
            },
            new VBox {
              alignment = Pos.Center
              spacing = 5
              children = Seq(
                new Text {
                  text = "DIFFICULTY"
                  font = Font.font("Arial", FontWeight.Bold, 12)
                  fill = lightBlue
                },
                createDifficultyIndicator(selectedQuestion.difficulty)
              )
            }
          )
        }
        
        // Crowd visualization
        val crowdBox = createCrowdVisual(true)
        
        // Question box with animation
        val questionBox = new VBox {
          alignment = Pos.Center
          padding = Insets(30, 40, 30, 40)
          style = s"""
            -fx-background-color: linear-gradient(to right, rgba(100, 100, 200, 0.2), rgba(150, 150, 255, 0.3));
            -fx-background-radius: 15;
            -fx-border-color: ${rgbToHex(lightBlue)};
            -fx-border-width: 2;
            -fx-border-radius: 15;
          """
          maxWidth = 800
          children = Seq(
            new Text {
              text = selectedQuestion.prompt
              font = Font.font("Arial", FontWeight.Bold, 24)
              fill = Color.White
              wrappingWidth = 720
              style = "-fx-text-alignment: center;"
            }
          )
        }
        
        // Scale in animation for question
        questionBox.setScaleX(0.8)
        questionBox.setScaleY(0.8)
        val scaleIn = new ScaleTransition(Duration(400), questionBox) {
          fromX = 0.8
          fromY = 0.8
          toX = 1.0
          toY = 1.0
        }
        Platform.runLater(() => scaleIn.play())
        
        // Options with staggered animation
        val optionsBox = new VBox {
          alignment = Pos.Center
          spacing = 12
          children = selectedQuestion.options.zipWithIndex.map { case (option, index) =>
            val letter = ('a' + index).toChar
            val btn = createOptionButton(option, letter.toString, selectedQuestion)
            btn.setOpacity(0)
            
            // Staggered fade in
            Platform.runLater(() => {
              Thread.sleep(index * 100)
              val fadeIn = new FadeTransition(Duration(300), btn) {
                fromValue = 0
                toValue = 1
              }
              fadeIn.play()
            })
            
            btn
          }
        }
        
        // Pass button
        val passBox = new HBox {
          alignment = Pos.Center
          visible = !userEliminated && !userPassUsed && remainingPlayers.length > (players.length * 0.25)
          children = Seq(
            new Button {
              text = "🎫 USE PASS"
              font = Font.font("Arial", FontWeight.Bold, 18)
              style = s"""
                -fx-background-color: linear-gradient(to bottom, #FF8C00, #FF6600);
                -fx-text-fill: white;
                -fx-padding: 12 35;
                -fx-background-radius: 20;
                -fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.3), 6, 0.3, 0, 0);
              """
              onMouseEntered = e => {
                style = s"""
                  -fx-background-color: linear-gradient(to bottom, #FFA500, #FF8C00);
                  -fx-text-fill: white;
                  -fx-padding: 12 35;
                  -fx-background-radius: 20;
                  -fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.5), 8, 0.4, 0, 0);
                  -fx-scale-x: 1.05;
                  -fx-scale-y: 1.05;
                """
              }
              onMouseExited = e => {
                style = s"""
                  -fx-background-color: linear-gradient(to bottom, #FF8C00, #FF6600);
                  -fx-text-fill: white;
                  -fx-padding: 12 35;
                  -fx-background-radius: 20;
                  -fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.3), 6, 0.3, 0, 0);
                """
              }
              onAction = _ => {
                userPassUsed = true
                transitionToScene(showRoundResults(selectedQuestion, "p"))
              }
            }
          )
        }
        
        // Status for eliminated players
        val statusBox = new HBox {
          alignment = Pos.Center
          visible = userEliminated
          children = Seq(
            new Text {
              text = "👀 You are eliminated - watching simulation"
              font = Font.font("Arial", FontWeight.Bold, 20)
              fill = Color.rgb(255, 150, 150)
            }
          )
        }
        
        children = Seq(progressBar, headerBox, crowdBox, questionBox, optionsBox, passBox, statusBox)
        
        // Auto-simulate if user is eliminated
        if (userEliminated) {
          Platform.runLater {
            Thread.sleep(2000)
            transitionToScene(showRoundResults(selectedQuestion, ""))
          }
        }
      }
    }
    
    scene
  }

  def createOptionButton(optionText: String, letter: String, question: Question): Button = {
    val letterUppercase = letter.toUpperCase
    val btn = new Button()
    
    // Create button content with letter circle
    val letterCircle = new Circle {
      radius = 25
      fill = lightBlue
      stroke = Color.White
      strokeWidth = 2
    }
    
    val letterText = new Text {
      text = letterUppercase
      font = Font.font("Arial", FontWeight.Bold, 20)
      fill = Color.White
    }
    
    val answerText = new Text {
      text = optionText
      font = Font.font("Arial", FontWeight.Bold, 18)
      fill = Color.White
      wrappingWidth = 650
    }
    
    val letterStack = new StackPane {
      children = Seq(letterCircle, letterText)
    }
    
    val contentBox = new HBox {
      alignment = Pos.CenterLeft
      spacing = 20
      padding = Insets(15, 25, 15, 25)
      children = Seq(letterStack, answerText)
    }
    
    btn.graphic = contentBox
    btn.prefWidth = 750
    btn.minHeight = 75
    btn.style = s"""
      -fx-background-color: linear-gradient(to right, rgba(70, 70, 150, 0.6), rgba(90, 90, 180, 0.7));
      -fx-background-radius: 12;
      -fx-border-color: ${rgbToHex(lightBlue)};
      -fx-border-width: 2;
      -fx-border-radius: 12;
      -fx-padding: 0;
    """
    
    btn.onMouseEntered = _ => {
      letterCircle.fill = primaryGold
      btn.style = s"""
        -fx-background-color: linear-gradient(to right, ${rgbToHex(lightBlue)}, rgba(150, 150, 255, 0.9));
        -fx-background-radius: 12;
        -fx-border-color: ${rgbToHex(primaryGold)};
        -fx-border-width: 3;
        -fx-border-radius: 12;
        -fx-effect: dropshadow(gaussian, rgba(100, 150, 255, 0.8), 15, 0.6, 0, 0);
        -fx-scale-x: 1.03;
        -fx-scale-y: 1.03;
        -fx-padding: 0;
      """
    }
    
    btn.onMouseExited = _ => {
      letterCircle.fill = lightBlue
      btn.style = s"""
        -fx-background-color: linear-gradient(to right, rgba(70, 70, 150, 0.6), rgba(90, 90, 180, 0.7));
        -fx-background-radius: 12;
        -fx-border-color: ${rgbToHex(lightBlue)};
        -fx-border-width: 2;
        -fx-border-radius: 12;
        -fx-padding: 0;
      """
    }
    
    btn.onAction = _ => {
      if (!userEliminated) {
        transitionToScene(showRoundResults(question, letter))
      }
    }
    
    btn
  }

  def showRoundResults(question: Question, userAnswer: String): Scene = {
    val isUserCorrect = if (userAnswer == "p") {
      true // Pass means user continues
    } else if (userAnswer.isEmpty) {
      !userEliminated // Already eliminated
    } else {
      userAnswer == question.answer.toString
    }
    
    if (!userEliminated && userAnswer != "p" && !isUserCorrect) {
      userEliminated = true
    }
    
    // Simulate CPU answers
    val currentEliminatedPlayers = Game.simulateAnswersGUI(question, remainingPlayers, userEliminated)
    
    val isNullRound = currentEliminatedPlayers.length == remainingPlayers.length
    
    if (!isNullRound) {
      remainingPlayers = remainingPlayers.filterNot(p => currentEliminatedPlayers.contains(p))
      eliminatedPlayers = eliminatedPlayers ++ currentEliminatedPlayers
    }
    
    createResultsScreen(question, userAnswer, currentEliminatedPlayers, isNullRound, isUserCorrect)
  }

  def createResultsScreen(question: Question, userAnswer: String, eliminated: List[Player], isNullRound: Boolean, isUserCorrect: Boolean): Scene = {
    val correctAnswerText = Game.getCorrectAnswer(question)
    val correctAnswerLetter = question.answer.toString
    
    val resultColor = if (userAnswer == "p") {
      Color.rgb(255, 165, 0)
    } else if (isUserCorrect) {
      Color.rgb(100, 255, 100)
    } else if (userEliminated && userAnswer.nonEmpty) {
      Color.rgb(255, 100, 100)
    } else {
      Color.White
    }
    
    val resultMsg = if (userAnswer == "p") {
      "🎫 PASS USED - You advance!"
    } else if (isUserCorrect && !userEliminated) {
      "✓ CORRECT!"
    } else if (userEliminated && userAnswer.nonEmpty) {
      "✗ INCORRECT - You are eliminated"
    } else {
      "Watching simulation..."
    }
    
    val baseChildren = Seq(
      new Text {
        text = "CORRECT ANSWER"
        font = Font.font("Arial", FontWeight.Bold, 26)
        fill = primaryGold
      },
      new VBox {
        alignment = Pos.Center
        padding = Insets(20)
        style = s"""
          -fx-background-color: rgba(255, 215, 0, 0.15);
          -fx-background-radius: 12;
          -fx-border-color: ${rgbToHex(primaryGold)};
          -fx-border-width: 2;
          -fx-border-radius: 12;
        """
        maxWidth = 750
        children = Seq(
          new Text {
            text = s"($correctAnswerLetter) $correctAnswerText"
            font = Font.font("Arial", FontWeight.Bold, 22)
            fill = Color.White
            wrappingWidth = 700
            style = "-fx-text-alignment: center;"
          }
        )
      },
      new Text {
        text = resultMsg
        font = Font.font("Arial", FontWeight.Bold, 26)
        fill = resultColor
      },
      new Separator {
        maxWidth = 700
        style = s"-fx-background-color: ${rgbToHex(lightBlue)};"
      }
    )
    
    // Crowd with updated eliminations
    val crowdBox = createCrowdVisual(true)
    
    val roundStatusChildren = if (isNullRound) {
      Seq(
        new VBox {
          alignment = Pos.Center
          spacing = 15
          padding = Insets(20)
          style = "-fx-background-color: rgba(255, 140, 0, 0.2); -fx-background-radius: 12;"
          children = Seq(
            new Text {
              text = "⚠️ NULL ROUND!"
              font = Font.font("Arial", FontWeight.Bold, 32)
              fill = Color.rgb(255, 165, 0)
            },
            new Text {
              text = "No one got the answer correct!"
              font = Font.font("Arial", FontWeight.Bold, 20)
              fill = Color.White
            },
            new Text {
              text = "Let's try another question..."
              font = Font.font("Arial", 18)
              fill = lightBlue
            }
          )
        }
      )
    } else {
      val eliminatedSection = if (eliminated.length > 0) {
        val eliminatedPercentage = (eliminated.length.toDouble / (remainingPlayers.length + eliminated.length) * 100).round.toInt
        Seq(
          new VBox {
            alignment = Pos.Center
            spacing = 15
            padding = Insets(20)
            style = "-fx-background-color: rgba(255, 50, 50, 0.15); -fx-background-radius: 10;"
            children = Seq(
              new Text {
                text = s"❌ ${eliminated.length} PLAYERS ELIMINATED"
                font = Font.font("Arial", FontWeight.Bold, 24)
                fill = Color.rgb(255, 100, 100)
                effect = new DropShadow {
                  color = Color.rgb(255, 100, 100, 0.4)
                  radius = 6
                }
              },
              new HBox {
                alignment = Pos.Center
                spacing = 15
                children = Seq(
                  new Text {
                    text = s"$eliminatedPercentage%"
                    font = Font.font("Arial", FontWeight.Bold, 32)
                    fill = wrongRed
                  },
                  new Text {
                    text = "of players got it wrong"
                    font = Font.font("Arial", FontWeight.Bold, 16)
                    fill = Color.rgb(255, 150, 150)
                  }
                )
              },
              new Rectangle {
                width = 600
                height = 12
                fill = Color.rgb(40, 40, 60)
                arcWidth = 6
                arcHeight = 6
                stroke = Color.rgb(80, 80, 100)
                strokeWidth = 1
              },
              new Rectangle {
                width = 600 * (eliminatedPercentage / 100.0)
                height = 12
                fill = wrongRed
                arcWidth = 6
                arcHeight = 6
                effect = new DropShadow {
                  color = Color.rgb(220, 20, 60, 0.5)
                  radius = 8
                }
              },
              new ScrollPane {
                prefHeight = 120
                prefWidth = 700
                style = s"""
                  -fx-background: transparent;
                  -fx-background-color: rgba(100, 50, 50, 0.2);
                  -fx-border-color: ${rgbToHex(Color.rgb(200, 100, 100))};
                  -fx-border-width: 1;
                  -fx-border-radius: 8;
                """
                content = new TextArea {
                  text = eliminated.map(_.name).mkString(", ")
                  editable = false
                  wrapText = true
                  style = s"""
                    -fx-control-inner-background: rgba(100, 50, 50, 0.2);
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                  """
                }
              }
            )
          }
        )
      } else {
        Seq(
          new Text {
            text = "✓ No players eliminated this round!"
            font = Font.font("Arial", FontWeight.Bold, 20)
            fill = Color.rgb(100, 255, 100)
          }
        )
      }
      
      eliminatedSection ++ Seq(
        new VBox {
          alignment = Pos.Center
          spacing = 8
          padding = Insets(15)
          style = s"""
            -fx-background-color: rgba(100, 255, 100, 0.1);
            -fx-background-radius: 10;
            -fx-border-color: ${rgbToHex(Color.rgb(100, 255, 100))};
            -fx-border-width: 2;
            -fx-border-radius: 10;
          """
          children = Seq(
            new Text {
              text = s"${remainingPlayers.length}"
              font = Font.font("Arial", FontWeight.Bold, 42)
              fill = Color.rgb(100, 255, 100)
            },
            new Text {
              text = "PLAYERS REMAINING"
              font = Font.font("Arial", FontWeight.Bold, 18)
              fill = lightBlue
            }
          )
        }
      )
    }
    
    val continueBtn = new Button {
      text = if (remainingPlayers.length == 1) "🏆 SEE WINNER" else "NEXT ROUND ▶"
      font = Font.font("Arial", FontWeight.Bold, 20)
      style = s"""
        -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
        -fx-text-fill: ${rgbToHex(darkBlue)};
        -fx-padding: 16 45;
        -fx-background-radius: 25;
        -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.3), 8, 0.3, 0, 0);
      """
      onMouseEntered = _ => {
        style = s"""
          -fx-background-color: linear-gradient(to bottom, #FFDD00, ${rgbToHex(primaryGold)});
          -fx-text-fill: ${rgbToHex(darkBlue)};
          -fx-padding: 16 45;
          -fx-background-radius: 25;
          -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.5), 10, 0.4, 0, 0);
          -fx-scale-x: 1.05;
          -fx-scale-y: 1.05;
        """
      }
      onMouseExited = _ => {
        style = s"""
          -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
          -fx-text-fill: ${rgbToHex(darkBlue)};
          -fx-padding: 16 45;
          -fx-background-radius: 25;
          -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.3), 8, 0.3, 0, 0);
        """
      }
      onAction = _ => {
        if (remainingPlayers.length == 1) {
          transitionToScene(createWinnerScreen())
        } else if (isNullRound) {
          currentRound -= 1 // Retry same round
          transitionToScene(createQuestionScreen())
        } else {
          transitionToScene(createQuestionScreen())
        }
      }
    }
    
    new Scene {
      fill = LinearGradient(
        startX = 0, startY = 0, endX = 0, endY = 1,
        proportional = true,
        cycleMethod = CycleMethod.NoCycle,
        stops = List(
          Stop(0, darkBlue),
          Stop(1, Color.rgb(20, 30, 60))
        )
      )
      
      content = new ScrollPane {
        fitToWidth = true
        style = "-fx-background: transparent; -fx-background-color: transparent;"
        content = new VBox {
          alignment = Pos.TopCenter
          spacing = 18
          padding = Insets(30, 20, 30, 20)
          children = baseChildren ++ Seq(crowdBox) ++ roundStatusChildren ++ Seq(continueBtn)
        }
      }
    }
  }

  def createWinnerScreen(): Scene = {
    val winner = remainingPlayers.head
    val scene = new Scene {
      fill = LinearGradient(
        startX = 0, startY = 0, endX = 0, endY = 1,
        proportional = true,
        cycleMethod = CycleMethod.NoCycle,
        stops = List(
          Stop(0, Color.rgb(10, 10, 30)),
          Stop(0.5, darkBlue),
          Stop(1, Color.rgb(30, 20, 50))
        )
      )
      
      content = new StackPane {
        val mainContent = new VBox {
          alignment = Pos.Center
          spacing = 30
          padding = Insets(50)
          
          val trophy = new Text {
            text = "🏆"
            font = Font.font("Arial", 100)
            effect = new DropShadow {
              color = Color.rgb(255, 215, 0, 0.6)
              radius = 20
              spread = 0.4
            }
          }
          
          // Enhanced pulsing animation for trophy
          val pulse = new ScaleTransition(Duration(800), trophy) {
            fromX = 1.0
            fromY = 1.0
            toX = 1.2
            toY = 1.2
            cycleCount = Timeline.Indefinite
            autoReverse = true
          }
          pulse.play()
          
          children = Seq(
            trophy,
            new Text {
              text = "THE 1%"
              font = Font.font("Arial", FontWeight.Bold, 72)
              fill = primaryGold
              effect = new DropShadow {
                color = Color.rgb(255, 215, 0, 0.5)
                radius = 15
                spread = 0.5
              }
            },
            new Text {
              text = "CLUB CHAMPION"
              font = Font.font("Arial", FontWeight.Bold, 36)
              fill = lightBlue
              effect = new DropShadow {
                color = Color.rgb(100, 150, 255, 0.4)
                radius = 10
              }
            },
            new Separator {
              maxWidth = 600
              style = s"-fx-background-color: ${rgbToHex(primaryGold)}; -fx-padding: 3;"
            },
            new VBox {
              alignment = Pos.Center
              spacing = 20
              padding = Insets(35)
              style = s"""
                -fx-background-color: radial-gradient(center 50% 50%, radius 80%, rgba(255, 215, 0, 0.2), rgba(255, 215, 0, 0.05));
                -fx-background-radius: 20;
                -fx-border-color: ${rgbToHex(primaryGold)};
                -fx-border-width: 4;
                -fx-border-radius: 20;
              """
              children = Seq(
                new Text {
                  text = "🌟 WINNER 🌟"
                  font = Font.font("Arial", FontWeight.Bold, 28)
                  fill = lightBlue
                },
                new Text {
                  text = winner.name
                  font = Font.font("Arial", FontWeight.Bold, 56)
                  fill = primaryGold
                  effect = new DropShadow {
                    color = Color.rgb(255, 215, 0, 0.5)
                    radius = 12
                  }
                },
                new Text {
                  text = if (winner.location.nonEmpty) s"📍 ${winner.location}, UK" else ""
                  font = Font.font("Arial", FontWeight.Bold, 24)
                  fill = Color.White
                  visible = winner.location.nonEmpty
                }
              )
            },
            new Separator {
              maxWidth = 600
              style = s"-fx-background-color: ${rgbToHex(lightBlue)}; -fx-padding: 2;"
            },
            new Text {
              text = if (winner.name == "You") "🎉 CONGRATULATIONS! You are the 1%! 🎉" else "Well played! Better luck next time!"
              font = Font.font("Arial", FontWeight.Bold, 28)
              fill = if (winner.name == "You") correctGreen else Color.rgb(255, 165, 0)
              wrappingWidth = 700
              textAlignment = TextAlignment.Center
              effect = new DropShadow {
                color = if (winner.name == "You") Color.rgb(50, 205, 50, 0.4) else Color.rgb(255, 165, 0, 0.3)
                radius = 8
              }
            },
            new Button {
              text = "🔄 PLAY AGAIN"
              font = Font.font("Arial", FontWeight.Bold, 22)
              style = s"""
                -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
                -fx-text-fill: ${rgbToHex(darkBlue)};
                -fx-padding: 20 55;
                -fx-background-radius: 30;
                -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 10, 0.4, 0, 0);
              """
              onMouseEntered = _ => {
                style = s"""
                  -fx-background-color: linear-gradient(to bottom, #FFDD00, ${rgbToHex(primaryGold)});
                  -fx-text-fill: ${rgbToHex(darkBlue)};
                  -fx-padding: 20 55;
                -fx-background-radius: 30;
                -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.5), 12, 0.5, 0, 0);
                -fx-scale-x: 1.05;
                -fx-scale-y: 1.05;
              """
              }
              onMouseExited = _ => {
                style = s"""
                  -fx-background-color: linear-gradient(to bottom, ${rgbToHex(primaryGold)}, #CC9900);
                  -fx-text-fill: ${rgbToHex(darkBlue)};
                  -fx-padding: 20 55;
                  -fx-background-radius: 30;
                  -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 10, 0.4, 0, 0);
                """
              }
              onAction = _ => {
                // Reset game state
                currentRound = 0
                userPassUsed = false
                userEliminated = false
                val initialPlayers = Player("You", "", 0, 0, 0, 0, 0, 0, 0, 0, 0) +: players
                remainingPlayers = initialPlayers
                allPlayers = initialPlayers
                eliminatedPlayers = List()
                transitionToScene(createIntroScreen())
              }
            }
          )
        }
        
        children = Seq(mainContent)
      }
    }
    
    // Enhanced confetti effect
    Platform.runLater(() => {
      val root = scene.root.value.asInstanceOf[javafx.scene.layout.StackPane]
      for (i <- 0 until 50) {
        Thread.sleep(i * 15)
        val confetti = new Circle {
          radius = Random.nextDouble() * 6 + 4
          fill = List(primaryGold, lightBlue, Color.rgb(255, 100, 255), correctGreen, Color.rgb(255, 150, 50))(Random.nextInt(5))
          layoutX = Random.nextDouble() * 1400
          layoutY = -30
          opacity = 1.0
        }
        
        root.getChildren.add(confetti)
        
        val duration = Duration(Random.nextDouble() * 3000 + 2000)
        val fall = new Timeline {
          keyFrames = Seq(
            KeyFrame(Duration(0), values = Set(
              KeyValue(confetti.layoutY, -30),
              KeyValue(confetti.opacity, 1.0)
            )),
            KeyFrame(duration * 0.9, values = Set(
              KeyValue(confetti.layoutY, 950),
              KeyValue(confetti.opacity, 1.0)
            )),
            KeyFrame(duration, values = Set(
              KeyValue(confetti.layoutY, 1000),
              KeyValue(confetti.opacity, 0.0)
            ))
          )
        }
        fall.onFinished = _ => root.getChildren.remove(confetti)
        fall.play()
      }
    })
    
    scene
  }

  def selectQuestion(questions: List[Question], difficulty: Int): Question = {
    val eligibleQuestions = questions.filter(_.difficulty == difficulty)
    Random.shuffle(eligibleQuestions).head
  }
}

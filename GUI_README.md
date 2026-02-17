# ONE% Game Show - Enhanced GUI Version

## 🎮 Running the Game

### GUI Version (ScalaFX - Enhanced ITV Style!)
```bash
cd /Users/neka/Projects/one-percent
sbt "runMain game.GameGUI"
```

Or use the quick launcher:
```bash
./run-gui.sh
```

### Console Version (Original)
```bash
sbt "runMain game.Game"
```

## ✨ New Enhanced Features

### Professional ITV 1% Club Styling
- **Gradient Backgrounds**: Dynamic dark blue gradient backgrounds matching the TV show aesthetic
- **Gold & Blue Theme**: Official ONE% color scheme with glowing effects
- **Smooth Animations**: Fade transitions between all screens
- **Hover Effects**: Interactive buttons with scale and glow animations

### 🎭 Crowd Visualization
The star feature! A live grid showing all 100 contestants:
- **Rows & Columns Layout**: 5 rows × 20 columns = 100 player positions
- **Color-Coded Status**:
  - 🔴 **Blue Light** = Eliminated players (dark blue)
  - ⚪ **White Light** = Active players (bright white with glow)
  - 🟡 **Gold** = You (the player) with special highlighting
- **Pulsing Animation**: Active players pulse to show they're still in the game
- **Live Updates**: The crowd updates in real-time as players are eliminated
- **Tooltips**: Hover over any circle to see the player's name

### 🎬 Smooth Transitions & Animations
- **Scene Transitions**: Elegant fade in/out between screens (300-800ms)
- **Question Reveal**: Questions scale in from 80% to 100%
- **Staggered Options**: Answer buttons fade in one by one (100ms delays)
- **Trophy Pulse**: Winner trophy scales and pulses continuously
- **Confetti Effect**: Falling colored circles celebrate the winner

### 🎨 Enhanced UI Components

#### Intro Screen
- Large glowing gold "ONE%" title with shadow effects
- Live crowd visualization showing all 100 players
- Professional gradient button with hover effects

#### Question Screen
- **Header Stats**: Shows round number, category, and players remaining
- **Live Crowd Grid**: See eliminations happen in real-time
- **Styled Question Box**: Border and gradient background
- **Modern Answer Buttons**: 
  - Gradient backgrounds
  - Border highlighting on hover
  - Glow effects
  - Scale animation (1.02x)
- **Pass Button**: Orange gradient with special styling

#### Results Screen
- **Answer Reveal**:

#### Results Screen
- **Answer Reveal**: Gold-bordered box with correct answer
- **Status Messages**: Color-coded (green=correct, red=eliminated, orange=pass)
- **Updated Crowd Grid**: See who got eliminated with blue lights
- **Elimination List**: Scrollable area showing all eliminated players
- **Player Count**: Large animated number showing remaining players
- **Smooth Continue**: Animated button to next round

#### Winner Screen
- **Pulsing Trophy**: 🏆 with continuous scale animation
- **Gradient Background**: Multi-stop gradient for celebration mood
- **Winner Spotlight**: Gold-bordered box highlighting the champion
- **Confetti Animation**: 30 falling colored circles
- **Play Again**: Reset and restart with all animations

## 🎮 Game Rules

### Game Rules
- 100 players start (99 CPU + You)
- 12 rounds with increasing difficulty: 5, 10, 15, 25, 30, 45, 50, 60, 75, 80, 90, 99
- Each question has 4 multiple choice options (a, b, c, d)
- **Pass**: Use once per game to skip a question (not available in final 25%)
- **Null Round**: If everyone gets it wrong, the round repeats with a new question
- **Elimination**: Get the answer wrong = eliminated (spectate the rest)
- **Goal**: Be the last player standing to become the ONE%!

### Categories
- (GEN) General Knowledge
- (SPO) Sports & Entertainment
- (MUS) Music & Arts
- (MAT) Mathematics & Geometry
- (LAN) Language & Literature
- (TEC) Technology & Science
- (GEO) Geography & Nature
- (HIS) History & Politics
- (REC) Religion & Culture

## 🛠 Technical Details

### Dependencies
- **Scala**: 2.13.12
- **ScalaFX**: 20.0.0-R31 (with full animation support)
- **JavaFX**: 20.0.1 (all modules: controls, graphics, media, etc.)

### New Imports & Features Used
```scala
import scalafx.animation.{FadeTransition, ScaleTransition, Timeline, KeyFrame, KeyValue}
import scalafx.scene.paint.{LinearGradient, Stop, CycleMethod}
import scalafx.scene.effect.DropShadow
import scalafx.scene.shape.Circle
```

### Project Structure
```
src/main/scala/game/
├── Game.scala         # Original console + shared game logic
│                      # Added simulateAnswersGUI() for GUI
├── GameGUI.scala      # Enhanced ScalaFX GUI (NEW & IMPROVED!)
│                      # Features:
│                      # - Crowd visualization grid
│                      # - Smooth scene transitions
│                      # - Animation system
│                      # - ITV-style color schemes
├── Player.scala       # Player data model
├── Question.scala     # Question data model
├── Category.scala     # Category data model
├── DataLoader.scala   # CSV data loading
└── csv/              # Game data files
    ├── players.csv
    ├── questions.csv
    └── categories.csv
```

## 🎯 Tips for Playing
1. **Watch the Crowd**: See the blue lights appear as players get eliminated
2. **Read Carefully**: Questions get progressively harder
3. **Save Your Pass**: Don't use it too early - save it for harder rounds
4. **Track Your Position**: You're the gold circle in the crowd grid
5. **Enjoy the Animations**: Smooth transitions make the experience TV-quality

## ⚠️ Troubleshooting

### JavaFX Warning
If you see:
```
WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @...'
```
You can **safely ignore it**. The GUI works perfectly! This is a JDK 9+ module system warning.

### App Reactivation Timeout
```
WARNING: Timeout while waiting for app reactivation
```
Also **safe to ignore**. This is a macOS-specific warning and doesn't affect functionality.

### Module Not Found
If you get errors about missing modules, run:
```bash
sbt clean
sbt compile
```

### Slow Performance
The animations use CPU/GPU. If experiencing lag:
1. Close other applications
2. Ensure JavaFX has hardware acceleration enabled
3. The animations will smooth out after the first few transitions

## 🎨 Customization

Want to adjust the look? Edit these variables in `GameGUI.scala`:
```scala
val primaryGold = Color.rgb(255, 215, 0)      // Main gold color
val darkBlue = Color.rgb(10, 15, 35)          // Background dark
val lightBlue = Color.rgb(100, 150, 255)      // Accent blue
val eliminatedBlue = Color.rgb(30, 50, 120)   // Eliminated players
val activeGlow = Color.rgb(255, 255, 255)     // Active players
```

## 🎬 Animation Details

### Transition Timings
- Scene fade out: 300ms
- Scene fade in: 300ms
- Initial screen fade: 800ms
- Question scale in: 400ms
- Option stagger: 100ms per button
- Trophy pulse: 1000ms cycle
- Crowd pulse: 1000ms cycle
- Confetti fall: 2000-5000ms random

### Hover Effects
All buttons have:
- Scale transformation (1.05x)
- Glow intensity increase
- Color brightening
- Smooth transitions

## 👨‍💻 Development

To modify the GUI, edit `GameGUI.scala`. The game uses ScalaFX's Scene pattern where each screen is a separate Scene that can be swapped out with smooth transitions.

Main screens:
- `createIntroScreen()` - Welcome screen with crowd
- `createQuestionScreen()` - Quiz interface with live crowd
- `createResultsScreen()` - Round results with updated crowd
- `createWinnerScreen()` - Final celebration with confetti
- `createCrowdVisual()` - Reusable crowd grid component

Helper methods:
- `transitionToScene()` - Smooth fade between scenes
- `rgbToHex()` - Convert Color to CSS hex
- `selectQuestion()` - Pick question by difficulty

Enjoy playing the enhanced ONE%! 🏆✨


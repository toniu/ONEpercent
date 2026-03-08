# ONE% — Game Show Simulator

A browser-based game show simulator inspired by ITV's The 1% Club, built with
Scala.js, Slinky and React. 100 contestants answer questions of increasing
difficulty across 12 rounds — be the last one standing to become the ONE%.

> **Built with:** Scala 2.13 · Scala.js · Slinky · React 17 · Webpack 5 · GitHub Actions · Cloudflare Pages

### [View Live Demo](https://one-percent.pages.dev)

---

## Table of Contents

- [Features](#features)
- [Tech Stack and Key Decisions](#tech-stack-and-key-decisions)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Deployment](#deployment)
- [Author](#author)

---

## Features

### Game Mechanics
- **12-Round Progression** — Difficulty scales from 5% to 99% across rounds, matching the TV show format
- **100 Contestants** — 99 CPU players and the user compete simultaneously with census-realistic demographics
- **Category System** — Nine trivia categories (General Knowledge, Sports, Music, Mathematics, Language, Technology, Geography, History, Religion & Culture) each affecting CPU player performance
- **Pass System** — Each player has one pass to guarantee survival in a round, disabled in the final 25 players
- **Null Rounds** — If every contestant answers incorrectly, the round repeats with a new question
- **Spectator Mode** — Eliminated players watch the remaining rounds play out

### ITV 1% Club Final Logic
- **Final Choice** — The sole survivor before the 99% round chooses to take the money or play the final
- **Multi-Winner Support** — If multiple contestants survive all 12 rounds, they share the victory
- **No Winners Scenario** — Handles the case where all contestants are eliminated

### User Interface
- **SVG Stickmen Crowd** — 5×20 grid of animated stickmen representing all 100 contestants with regional colour coding and skin-tone diversity
- **Progress Bar** — Visual round tracker with colour-coded stages (completed, current, upcoming)
- **Countdown Timer** — Animated timer with colour transitions for each question
- **Custom SVG Icons** — All interface icons are inline SVGs with no external icon dependencies
- **Responsive Design** — Three breakpoints for width and height ensure the layout works across screen sizes
- **Fade Transitions** — Smooth slide-up and fade-in animations between screens

### Visual Design
- **Dark Theme** — Lime-green and dark palette inspired by the show's aesthetic
- **Rajdhani + Orbitron** — Custom typography pairing for body text and display headings
- **Winner Celebration** — Confetti animation, trophy icon and champion card on the final screen
- **Eliminated List** — Scrollable list of eliminated players with mini stickman avatars

---

## Tech Stack and Key Decisions

| Layer | Technology | Why |
|-------|-----------|-----|
| **Language** | Scala 2.13 | Functional programming with strong typing for game logic |
| **Compiler** | Scala.js 1.16 | Compiles Scala to optimised JavaScript for browser execution |
| **UI Library** | Slinky 0.7.4 | Type-safe React bindings for Scala.js with functional components |
| **Rendering** | React 17 | Component-based rendering with hooks for state management |
| **Bundler** | Webpack 5 | Module bundling with library-and-application mode via sbt-scalajs-bundler |
| **Build Tool** | sbt 1.9.9 | Scala build orchestration with Scala.js and bundler plugins |
| **CI/CD** | GitHub Actions | Automated build and deploy on push to main |
| **Hosting** | Cloudflare Pages | Static site hosting with global CDN |

### Notable Design Patterns

- **Immutable Game State** — All state transitions produce new `GameState` instances, making the game logic predictable and testable
- **Component Architecture** — Each screen (Intro, Question, Results, FinalChoice, Winner) is an isolated functional component with typed props
- **SVG Icon System** — A centralised `SvgIcons` object provides all icons as inline SVG strings, eliminating external icon dependencies
- **Census-Realistic Data** — 166 players with demographically representative names, ethnicities, skin tones and regional distribution across the UK
- **Probability-Based Simulation** — CPU player outcomes are calculated from their category ratings and question difficulty, producing realistic elimination patterns

---

## Screenshots

![ONE%](screenshots/project-onepercent.png)

---

## Getting Started

### Prerequisites

- [Java 17](https://adoptium.net/) (Temurin recommended)
- [sbt](https://www.scala-sbt.org/) (1.9+)
- [Node.js](https://nodejs.org/) (v20 LTS)

### Installation

```bash
# Clone the repository
git clone https://github.com/toniu/one-percent.git
cd one-percent

# Build the Scala.js bundle
npm run build

# Copy assets and start the local server
npm start
```

The game will be available at `http://localhost:8080`.

### npm Scripts

| Script | Description |
|--------|-------------|
| `npm run build` | Compile Scala.js and bundle with Webpack |
| `npm start` | Build, copy assets and serve locally on port 8080 |
| `npm run dev` | Stop any running server, rebuild and serve |
| `npm run dist` | Build and assemble the `dist/` folder for deployment |
| `npm run deploy` | Build and deploy to Cloudflare Pages (requires `wrangler login`) |

---

## Project Structure

```
one-percent/
├── src/main/
│   ├── scala/game/
│   │   ├── Category.scala              # Category enum and mappings
│   │   ├── GameApp.scala               # Scala.js entry point
│   │   ├── GameData.scala              # Embedded CSV player and question data
│   │   ├── GameLogic.scala             # Simulation engine and difficulty ratings
│   │   ├── GameState.scala             # Immutable game state model
│   │   ├── Player.scala                # Player case class with demographics
│   │   ├── Question.scala              # Question data model
│   │   └── components/
│   │       ├── App.scala               # Root component with game flow logic
│   │       ├── CrowdGrid.scala         # SVG stickmen crowd visualisation
│   │       ├── FinalChoiceScreen.scala  # Take-the-money-or-play choice
│   │       ├── IntroScreen.scala        # Welcome screen with start button
│   │       ├── QuestionScreen.scala     # Question display with timer and options
│   │       ├── ResultsScreen.scala      # Round results and elimination details
│   │       ├── SvgIcons.scala           # Centralised inline SVG icon library
│   │       └── WinnerScreen.scala       # Champion celebration with confetti
│   └── resources/
│       ├── index.html                   # HTML entry point
│       ├── style.css                    # Stylesheet with responsive breakpoints
│       └── logos/                       # Favicon and logo assets
├── build.sbt                            # Scala.js and bundler configuration
├── package.json                         # npm scripts for build and deploy
└── .github/workflows/
    └── deploy.yml                       # CI/CD pipeline for Cloudflare Pages
```

---

## Deployment

The project deploys automatically via GitHub Actions on every push to `main`.

To deploy manually:

```bash
npm run deploy
```

This requires a Cloudflare account. Run `npx wrangler login` first to authenticate.

For CI/CD, add the following secrets to your GitHub repository:

| Secret | Value |
|--------|-------|
| `CLOUDFLARE_API_TOKEN` | Cloudflare API token with Pages permissions |
| `CLOUDFLARE_ACCOUNT_ID` | Your Cloudflare account ID |

---

## Author

**Neka Toni-Uebari**

- GitHub: [toniu](https://github.com/toniu)

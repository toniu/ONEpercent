package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import scala.scalajs.js
import game._

/** Displays the current question with answer options, countdown timer, and action buttons. */
@react object QuestionScreen {
  case class Props(gs: GameState, onAnswer: String => Unit, onAutoSimulate: () => Unit)

  private val TotalTime = 30
  private val Radius    = 54.0
  private val Circumference = 2 * math.Pi * Radius

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val gs = props.gs
    val question = gs.currentQuestion.get
    val canPass = !gs.userEliminated && !gs.userPassUsed &&
      gs.remainingPlayers.length > (gs.allPlayers.length * 0.25)

    /* ── Local state ───────────────────────────────────── */
    val (selectedAnswer, setSelectedAnswer) = useState[Option[String]](None)
    val (timeLeft, setTimeLeft)             = useState(TotalTime)
    val (confirmed, setConfirmed)           = useState(false)
    val (visibleOptions, setVisibleOptions) = useState(0)
    val (readingPhase, setReadingPhase)      = useState(true)
    val timeRef      = useRef(TotalTime)
    val confirmedRef = useRef(false)

    /* ── Reset on new round ────────────────────────────── */
    useEffect(() => {
      setSelectedAnswer(None)
      setTimeLeft(TotalTime)
      setConfirmed(false)
      setVisibleOptions(0)
      setReadingPhase(true)
      timeRef.current = TotalTime
      confirmedRef.current = false
      ()
    }, Seq(gs.currentRound))

    val totalOptions = question.options.length

    /* ── Staged reveal: show options one by one, then start countdown ── */
    useEffect(() => {
      if (visibleOptions < totalOptions) {
        val delay = if (visibleOptions == 0) 3000 else 500
        val t = js.timers.setTimeout(delay.toDouble) { setVisibleOptions(visibleOptions + 1) }
        () => js.timers.clearTimeout(t)
      } else if (readingPhase) {
        val t2 = js.timers.setTimeout(1000) { setReadingPhase(false) }
        () => js.timers.clearTimeout(t2)
      } else {
        () => ()
      }
    }, Seq(visibleOptions, readingPhase, gs.currentRound))

    /* ── Countdown timer (active players only, after reading phase) ── */
    useEffect(() => {
      if (!gs.userEliminated && !confirmed && !readingPhase) {
        val intervalId = js.timers.setInterval(1000) {
          if (!confirmedRef.current) {
            timeRef.current = timeRef.current - 1
            setTimeLeft(timeRef.current)
            if (timeRef.current <= 0) {
              confirmedRef.current = true
              props.onAnswer("timeout")
            }
          }
        }
        () => js.timers.clearInterval(intervalId)
      } else {
        () => ()
      }
    }, Seq(gs.userEliminated, confirmed, readingPhase, gs.currentRound))

    def handleConfirm(): Unit = {
      selectedAnswer.foreach { answer =>
        confirmedRef.current = true
        setConfirmed(true)
        props.onAnswer(answer)
      }
    }

    /* ── Countdown ring values ─────────────────────────── */
    val progress   = timeLeft.toDouble / TotalTime
    val dashOffset = Circumference * (1.0 - progress)
    val timerColor = if (timeLeft > 10) "var(--lime)"
                     else if (timeLeft > 5) "var(--orange)"
                     else "var(--wrong-red)"

    div(className := "screen question-screen fade-in")(
      /* Mini logo */
      div(className := "mini-logo-bar")(
        img(src := "logos/one-percent-logo-notext.png", alt := "ONE%", className := "mini-logo")
      ),

      /* Progress bar */
      ProgressBar(ProgressBar.Props(gs.currentRound)),

      /* Crowd grid */
      CrowdGrid(CrowdGrid.Props(gs.allPlayers, gs.remainingPlayers, gs.eliminatedPlayers)),

      /* Header */
      div(className := "q-header")(
        div(className := "q-header-left")(
          h2(className := "round-label")(s"ROUND ${gs.currentRound}"),
          span(className := "category-badge")(
            span(className := "cat-icon", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.categoryIcon(question.category))),
            span(GameLogic.getCategoryName(question.category).toUpperCase)
          )
        ),
        div(className := "q-header-center")(
          span(className := "players-count")(gs.remainingPlayers.length.toString),
          span(className := "players-label")("PLAYERS LEFT")
        ),
        div(className := "q-header-right")(
          span(className := "diff-label")("DIFFICULTY"),
          DifficultyDots(DifficultyDots.Props(question.difficulty))
        )
      ),

      /* Countdown ring timer (active players only) */
      if (!gs.userEliminated && !confirmed) {
        val readingClass = if (readingPhase) " countdown-paused" else ""
        div(className := s"countdown-container${if (timeLeft <= 5) " countdown-critical" else ""}$readingClass",
          dangerouslySetInnerHTML := js.Dynamic.literal("__html" ->
            s"""<svg viewBox="0 0 120 120" class="countdown-ring">
               |  <circle class="countdown-bg" cx="60" cy="60" r="$Radius"/>
               |  <circle class="countdown-fg" cx="60" cy="60" r="$Radius"
               |    style="stroke-dasharray:${Circumference};stroke-dashoffset:${dashOffset};stroke:${timerColor}"/>
               |  <text x="60" y="66" class="countdown-text" fill="white"
               |    text-anchor="middle" font-size="32" font-weight="900"
               |    font-family="Inter,sans-serif">${timeLeft}</text>
               |</svg>""".stripMargin
          )
        )
      } else {
        div()
      },

      /* Question box */
      div(className := "question-box scale-in")(
        p(className := "question-text")(question.prompt)
      ),

      /* Options — staged reveal one by one: hidden → reading → interactive */
      div(className := s"options-container${if (gs.userEliminated || confirmed) " options-spectator" else if (readingPhase) " options-reading" else ""}")(
        question.options.zipWithIndex.map { case (opt, idx) =>
          val letter = ('a' + idx).toChar.toString
          val isSelected = selectedAnswer.contains(letter)
          val selClass = if (isSelected) " selected" else ""
          val visible = idx < visibleOptions
          div(
            key := letter,
            className := s"option-btn${if (visible) s" stagger-$idx" else " option-unrevealed"}$selClass",
            onClick := (_ => if (visible && !gs.userEliminated && !confirmed && !readingPhase) {
              setSelectedAnswer(
                if (selectedAnswer.contains(letter)) None else Some(letter)
              )
            })
          )(
            span(className := "option-letter")(letter.toUpperCase),
            span(className := "option-text")(opt)
          )
        }
      ),

      /* ── Action buttons ──────────────────────────────── */
      if (gs.userEliminated) {
        /* Spectator: skip button */
        div(className := "spectator-actions")(
          div(className := "status-eliminated")(
            span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.eye)),
            span(" You are eliminated \u2014 watching as spectator")
          ),
          button(
            className := "btn btn-skip",
            onClick := (_ => props.onAutoSimulate())
          )(
            span(className := "btn-icon-text")(
              span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.skipForward)),
              span(" SKIP")
            )
          )
        )
      } else if (!confirmed) {
        /* Active: confirm + optional pass */
        div(className := "action-buttons")(
          if (selectedAnswer.isDefined) {
            button(
              className := "btn btn-confirm pulse",
              onClick := (_ => handleConfirm())
            )(
              span(className := "btn-icon-text")(
                span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.checkCircle)),
                span(" CONFIRM ANSWER")
              )
            )
          } else {
            div()
          },
          if (canPass) {
            button(
              className := "btn btn-orange",
              onClick := (_ => { confirmedRef.current = true; setConfirmed(true); props.onAnswer("p") })
            )(
              span(className := "btn-icon-text")(
                span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.ticket)),
                span(" USE PASS")
              )
            )
          } else {
            div()
          }
        )
      } else {
        /* After confirming — waiting for transition */
        div(className := "status-confirmed")(
          span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.checkCircle)),
          span(" Answer locked in!")
        )
      }
    )
  }
}

/** Round progress bar showing difficulty tiers. */
@react object ProgressBar {
  case class Props(currentRound: Int)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    div(className := "progress-bar")(
      GameLogic.difficulties.zipWithIndex.map { case (diff, idx) =>
        val cls = if (idx < props.currentRound - 1) "prog-done"
                  else if (idx == props.currentRound - 1) "prog-active"
                  else "prog-future"
        div(key := idx.toString, className := s"prog-step $cls")(
          div(className := "prog-pip"),
          span(className := "prog-label")(diff.toString)
        )
      }
    )
  }
}

/** Difficulty dot indicator (1-5 dots). */
@react object DifficultyDots {
  case class Props(difficulty: Int)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val numDots = math.max(1, math.min(5, math.ceil(props.difficulty / 100.0 * 5).toInt))
    div(className := "diff-dots")(
      (1 to 5).map { i =>
        span(key := i.toString, className := (if (i <= numDots) "dot dot-active" else "dot"))
      }
    )
  }
}

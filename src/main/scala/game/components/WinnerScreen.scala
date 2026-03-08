package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import scala.scalajs.js
import game.Player

/** Winner celebration screen with confetti. */
@react object WinnerScreen {
  case class Props(winner: Player, onReset: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val w = props.winner
    val isYou = w.name == "You"

    /* Create confetti pieces on mount */
    val (confetti, _) = useState {
      (0 until 60).map { i =>
        val left  = (scala.util.Random.nextDouble() * 100).toInt
        val delay = (scala.util.Random.nextDouble() * 3).toInt
        val dur   = (scala.util.Random.nextDouble() * 3 + 2).toInt
        val size  = (scala.util.Random.nextDouble() * 8 + 4).toInt
        val hue   = scala.util.Random.nextInt(360)
        (left, delay, dur, size, hue)
      }.toList
    }

    div(className := "screen winner-screen fade-in")(
      /* Confetti layer */
      div(className := "confetti-container")(
        confetti.zipWithIndex.map { case ((left, delay, dur, size, hue), idx) =>
          div(
            key := idx.toString,
            className := "confetti-piece",
            style := js.Dynamic.literal(
              "left"              -> s"$left%",
              "animationDelay"    -> s"${delay}s",
              "animationDuration" -> s"${dur}s",
              "width"             -> s"${size}px",
              "height"            -> s"${size}px",
              "backgroundColor"  -> s"hsl($hue, 80%, 60%)"
            )
          )
        }
      ),

      /* Main content */
      div(className := "winner-content")(
        div(className := "trophy pulse")(
          "\uD83C\uDFC6"
        ),
        h1(className := "winner-title")("THE 1%"),
        h2(className := "winner-subtitle")("CLUB CHAMPION"),
        div(className := "separator"),
        div(className := "winner-card")(
          p(className := "winner-tag")("\uD83C\uDF1F WINNER \uD83C\uDF1F"),
          h1(className := "winner-name")(w.name),
          if (w.location.nonEmpty)
            p(className := "winner-location")(s"\uD83D\uDCCD ${w.location}, UK")
          else div()
        ),
        div(className := "separator"),
        p(className := (if (isYou) "congrats-text you-win" else "congrats-text"))(
          if (isYou) "\uD83C\uDF89 CONGRATULATIONS! You are the 1%! \uD83C\uDF89"
          else "Well played! Better luck next time!"
        ),
        button(
          className := "btn btn-gold btn-large",
          onClick := (_ => props.onReset())
        )("\uD83D\uDD04 PLAY AGAIN")
      )
    )
  }
}

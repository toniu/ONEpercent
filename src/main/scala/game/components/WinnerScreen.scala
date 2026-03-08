package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import scala.scalajs.js
import game.Player

/** Winner celebration screen with confetti — supports multiple winners. */
@react object WinnerScreen {
  case class Props(winners: List[Player], onReset: () => Unit)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val winners = props.winners
    val youWon  = winners.exists(_.name == "You")
    val noWinners = winners.isEmpty

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
      if (!noWinners) {
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
        )
      } else div(),

      /* Main content */
      div(className := "winner-content slide-up")(
        div(className := "mini-logo-bar")(
          img(src := "logos/one-percent-logo-notext.png", alt := "ONE%", className := "mini-logo")
        ),

        if (noWinners) {
          /* No one survived */
          span(
            div(className := "trophy-icon",
              dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophyLarge)
            ),
            h1(className := "winner-title")("NO WINNERS"),
            p(className := "winner-subtitle")("EVERYONE ELIMINATED"),
            div(className := "separator"),
            p(className := "congrats-text")("Nobody made it through! Better luck next time.")
          )
        } else if (winners.length == 1) {
          /* Single winner */
          val w = winners.head
          val isYou = w.name == "You"
          val headColor = if (isYou) "#c3fc02" else w.skinTone
          val bodyColor = if (isYou) "#c3fc02" else RegionColors.get(w.region)
          span(
            div(className := "trophy-icon pulse",
              dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophyLarge)
            ),
            h1(className := "winner-title")("THE ONE%"),
            p(className := "winner-subtitle")("CHAMPION"),
            div(className := "separator"),
            div(className := "winner-card")(
              div(className := "winner-avatar-wrap",
                dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.winnerAvatar(headColor, bodyColor))
              ),
              div(className := "winner-details")(
                p(className := "winner-tag")(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.star)),
                  span(" WINNER "),
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.star))
                ),
                h1(className := "winner-name")(w.name),
                if (w.location.nonEmpty)
                  p(className := "winner-location")(
                    span(className := "icon-inline icon-pin", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.mapPin)),
                    span(s" ${w.location}, UK")
                  )
                else div()
              )
            ),
            div(className := "separator"),
            p(className := (if (isYou) "congrats-text you-win" else "congrats-text"))(
              if (isYou) {
                span(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.sparkles)),
                  span(" CONGRATULATIONS! You are the 1%! "),
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.sparkles))
                )
              } else {
                span("Well played! Better luck next time!")
              }
            )
          )
        } else {
          /* Multiple winners */
          span(
            div(className := "trophy-icon pulse",
              dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.trophyLarge)
            ),
            h1(className := "winner-title")(s"THE ${winners.length}%"),
            p(className := "winner-subtitle")(s"${winners.length} CHAMPIONS"),
            div(className := "separator"),
            div(className := "winners-list")(
              winners.map { w =>
                val isYou = w.name == "You"
                val headColor = if (isYou) "#c3fc02" else w.skinTone
                val bodyColor = if (isYou) "#c3fc02" else RegionColors.get(w.region)
                div(key := w.name, className := s"winner-card${if (isYou) " winner-card-you" else ""}")(
                  div(className := "winner-avatar-wrap",
                    dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.winnerAvatar(headColor, bodyColor))
                  ),
                  div(className := "winner-details")(
                    p(className := "winner-tag")(
                      span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.star)),
                      span(if (isYou) " YOU " else " WINNER "),
                      span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.star))
                    ),
                    h2(className := "winner-name")(w.name),
                    if (w.location.nonEmpty)
                      p(className := "winner-location")(
                        span(className := "icon-inline icon-pin", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.mapPin)),
                        span(s" ${w.location}, UK")
                      )
                    else div()
                  )
                )
              }
            ),
            div(className := "separator"),
            p(className := (if (youWon) "congrats-text you-win" else "congrats-text"))(
              if (youWon) {
                span(
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.sparkles)),
                  span(s" CONGRATULATIONS! You share the glory with ${winners.length - 1} other${if (winners.length > 2) "s" else ""}! "),
                  span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.sparkles))
                )
              } else {
                span(s"${winners.length} players made it through! Better luck next time!")
              }
            )
          )
        },

        button(
          className := "btn btn-gold btn-large",
          onClick := (_ => props.onReset())
        )(
          span(className := "btn-icon-text")(
            span(className := "icon-inline", dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> SvgIcons.refresh)),
            span(" PLAY AGAIN")
          )
        )
      )
    )
  }
}

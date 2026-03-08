package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import game._

/** Crowd visualization grid — 5 rows x 20 columns of player circles. */
@react object CrowdGrid {
  case class Props(
    allPlayers: List[Player],
    remainingPlayers: List[Player],
    eliminatedPlayers: List[Player]
  )

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val cols  = 20
    val total = 100

    div(className := "crowd-grid")(
      (0 until total).map { i =>
        val playerOpt   = if (i < props.allPlayers.length) Some(props.allPlayers(i)) else None
        val isEliminated = playerOpt.exists(p => props.eliminatedPlayers.contains(p))
        val isActive     = playerOpt.exists(p => props.remainingPlayers.contains(p))
        val isYou        = playerOpt.exists(_.name == "You")

        val cls = if (playerOpt.isEmpty) "circle empty"
                  else if (isEliminated) "circle eliminated"
                  else if (isYou) "circle you"
                  else if (isActive) "circle active"
                  else "circle empty"

        val tooltipName = playerOpt.map(_.name).getOrElse("Empty")

        span(
          key := i.toString,
          className := cls,
          title := tooltipName
        )
      }
    )
  }
}

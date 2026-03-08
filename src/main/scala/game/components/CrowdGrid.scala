package game.components

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import game._

/** Region code → torso colour mapping (UK regions). */
object RegionColors {
  val colors: Map[String, String] = Map(
    "LON" -> "#E63946",
    "SE"  -> "#457B9D",
    "SW"  -> "#2A9D8F",
    "E"   -> "#E9C46A",
    "EM"  -> "#F4A261",
    "WM"  -> "#6D597A",
    "NW"  -> "#6A4C93",
    "NE"  -> "#1982C4",
    "YH"  -> "#B5838D",
    "WA"  -> "#FF006E",
    "SC"  -> "#3A86FF",
    "NI"  -> "#FB5607"
  )

  def get(region: String): String = colors.getOrElse(region, "#888888")
}

/** Crowd visualization grid — 5 rows x 20 columns of mini stickmen SVGs. */
@react object CrowdGrid {
  case class Props(
    allPlayers: List[Player],
    remainingPlayers: List[Player],
    eliminatedPlayers: List[Player]
  )

  private val lime       = "#c3fc02"
  private val elimGray   = "#3a3d36"
  private val emptyColor = "rgba(60,60,50,0.25)"

  private def stickmanSvg(headColor: String, bodyColor: String, glow: Boolean): String = {
    val glowFilter = if (glow)
      """<defs><filter id="lime-glow" x="-50%" y="-50%" width="200%" height="200%">
         |  <feGaussianBlur stdDeviation="2" result="blur"/>
         |  <feFlood flood-color="#c3fc02" flood-opacity="0.6" result="color"/>
         |  <feComposite in="color" in2="blur" operator="in" result="shadow"/>
         |  <feMerge><feMergeNode in="shadow"/><feMergeNode in="SourceGraphic"/></feMerge>
         |</filter></defs>""".stripMargin
    else ""
    val filterAttr = if (glow) """ filter="url(#lime-glow)" """ else ""
    s"""<svg viewBox="0 0 20 26" class="stickman">
       |  $glowFilter
       |  <g$filterAttr>
       |    <circle cx="10" cy="5.5" r="4.5" fill="$headColor"/>
       |    <rect x="5" y="11" width="10" height="13" rx="3" fill="$bodyColor"/>
       |  </g>
       |</svg>""".stripMargin
  }

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val total = 100

    div(className := "crowd-grid")(
      (0 until total).map { i =>
        val playerOpt    = if (i < props.allPlayers.length) Some(props.allPlayers(i)) else None
        val isEliminated = playerOpt.exists(p => props.eliminatedPlayers.contains(p))
        val isActive     = playerOpt.exists(p => props.remainingPlayers.contains(p))
        val isYou        = playerOpt.exists(_.name == "You")

        val tooltipName = playerOpt.map(p => s"${p.name} — ${p.location}").getOrElse("Empty")

        val (svgHtml, cls) = playerOpt match {
          case None =>
            (stickmanSvg(emptyColor, emptyColor, glow = false), "stickman-cell empty")
          case Some(p) if isYou && isActive =>
            (stickmanSvg(lime, lime, glow = true), "stickman-cell you")
          case Some(p) if isYou && isEliminated =>
            (stickmanSvg(elimGray, elimGray, glow = false), "stickman-cell eliminated")
          case Some(p) if isEliminated =>
            (stickmanSvg(elimGray, elimGray, glow = false), "stickman-cell eliminated")
          case Some(p) if isActive =>
            val bodyColor = RegionColors.get(p.region)
            (stickmanSvg(p.skinTone, bodyColor, glow = false), "stickman-cell active")
          case _ =>
            (stickmanSvg(emptyColor, emptyColor, glow = false), "stickman-cell empty")
        }

        div(
          key := i.toString,
          className := cls,
          title := tooltipName,
          dangerouslySetInnerHTML := js.Dynamic.literal("__html" -> svgHtml)
        )
      }
    )
  }
}

package game.components

/** Inline SVG icon strings — lightweight replacements for emoji throughout the app.
  * Each icon is a self-contained `<svg>` element sized to fit inline with text.
  */
object SvgIcons {

  /** Trophy cup — winner screen & "SEE WINNER" button. */
  val trophy: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"/>
      |  <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"/>
      |  <path d="M4 22h16"/>
      |  <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20 7 22"/>
      |  <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20 17 22"/>
      |  <path d="M18 2H6v7a6 6 0 0 0 12 0V2Z"/>
      |</svg>""".stripMargin

  /** Large trophy for the winner celebration. */
  val trophyLarge: String =
    """<svg viewBox="0 0 24 24" class="icon-trophy-lg" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"/>
      |  <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"/>
      |  <path d="M4 22h16"/>
      |  <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20 7 22"/>
      |  <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20 17 22"/>
      |  <path d="M18 2H6v7a6 6 0 0 0 12 0V2Z"/>
      |</svg>""".stripMargin

  /** Star — winner tag. */
  val star: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="currentColor">
      |  <polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26"/>
      |</svg>""".stripMargin

  /** Map pin — location. */
  val mapPin: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
      |  <circle cx="12" cy="10" r="3"/>
      |</svg>""".stripMargin

  /** Check circle — correct / confirm. */
  val checkCircle: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
      |  <polyline points="22 4 12 14.01 9 11.01"/>
      |</svg>""".stripMargin

  /** X circle — wrong / eliminated. */
  val xCircle: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      |  <circle cx="12" cy="12" r="10"/>
      |  <line x1="15" y1="9" x2="9" y2="15"/>
      |  <line x1="9" y1="9" x2="15" y2="15"/>
      |</svg>""".stripMargin

  /** Clock — timeout. */
  val clock: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <circle cx="12" cy="12" r="10"/>
      |  <polyline points="12 6 12 12 16 14"/>
      |</svg>""".stripMargin

  /** Ticket — pass. */
  val ticket: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M2 9a3 3 0 0 1 0 6v2a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a3 3 0 0 1 0-6V7a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2Z"/>
      |  <path d="M13 5v2"/>
      |  <path d="M13 17v2"/>
      |  <path d="M13 11v2"/>
      |</svg>""".stripMargin

  /** Alert triangle — null round. */
  val alertTriangle: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
      |  <line x1="12" y1="9" x2="12" y2="13"/>
      |  <line x1="12" y1="17" x2="12.01" y2="17"/>
      |</svg>""".stripMargin

  /** Eye — spectator. */
  val eye: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8Z"/>
      |  <circle cx="12" cy="12" r="3"/>
      |</svg>""".stripMargin

  /** Skip forward — skip button. */
  val skipForward: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <polygon points="5 4 15 12 5 20 5 4"/>
      |  <line x1="19" y1="5" x2="19" y2="19"/>
      |</svg>""".stripMargin

  /** Chevron right — next round. */
  val chevronRight: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      |  <polyline points="9 18 15 12 9 6"/>
      |</svg>""".stripMargin

  /** Refresh — play again. */
  val refresh: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M21 12a9 9 0 1 1-9-9c2.52 0 4.93 1 6.74 2.74L21 8"/>
      |  <path d="M21 3v5h-5"/>
      |</svg>""".stripMargin

  /** Sparkles — celebration. */
  val sparkles: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="currentColor">
      |  <path d="M12 2l1.09 3.41L16.5 6.5l-3.41 1.09L12 11l-1.09-3.41L7.5 6.5l3.41-1.09L12 2Z"/>
      |  <path d="M19 11l.72 2.28L22 14l-2.28.72L19 17l-.72-2.28L16 14l2.28-.72L19 11Z"/>
      |  <path d="M5 17l.54 1.46L7 19l-1.46.54L5 21l-.54-1.46L3 19l1.46-.54L5 17Z"/>
      |</svg>""".stripMargin

  /** No-elim check — all safe. */
  val shieldCheck: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z"/>
      |  <polyline points="9 12 11 14 15 10"/>
      |</svg>""".stripMargin

  /* ── Category icons ────────────────────────────────── */

  /** Brain — General Knowledge. */
  val brain: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M9.5 2A2.5 2.5 0 0 1 12 4.5v15a2.5 2.5 0 0 1-4.96.44 2.5 2.5 0 0 1-2.96-3.08 3 3 0 0 1-.34-5.58 2.5 2.5 0 0 1 1.32-4.24 2.5 2.5 0 0 1 1.98-3A2.5 2.5 0 0 1 9.5 2Z"/>
      |  <path d="M14.5 2A2.5 2.5 0 0 0 12 4.5v15a2.5 2.5 0 0 0 4.96.44 2.5 2.5 0 0 0 2.96-3.08 3 3 0 0 0 .34-5.58 2.5 2.5 0 0 0-1.32-4.24 2.5 2.5 0 0 0-1.98-3A2.5 2.5 0 0 0 14.5 2Z"/>
      |</svg>""".stripMargin

  /** Football — Sports. */
  val football: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2">
      |  <circle cx="12" cy="12" r="10"/>
      |  <path d="M12 2a14.5 14.5 0 0 0 0 20"/>
      |  <path d="M12 2a14.5 14.5 0 0 1 0 20"/>
      |  <path d="M2 12h20"/>
      |</svg>""".stripMargin

  /** Music note — Music & Arts. */
  val music: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M9 18V5l12-2v13"/>
      |  <circle cx="6" cy="18" r="3"/>
      |  <circle cx="18" cy="16" r="3"/>
      |</svg>""".stripMargin

  /** Hash — Mathematics. */
  val hash: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <line x1="4" y1="9" x2="20" y2="9"/>
      |  <line x1="4" y1="15" x2="20" y2="15"/>
      |  <line x1="10" y1="3" x2="8" y2="21"/>
      |  <line x1="16" y1="3" x2="14" y2="21"/>
      |</svg>""".stripMargin

  /** Book — Language & Literature. */
  val book: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
      |  <path d="M4 4.5A2.5 2.5 0 0 1 6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15Z"/>
      |</svg>""".stripMargin

  /** Monitor — Technology & Science. */
  val monitor: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
      |  <line x1="8" y1="21" x2="16" y2="21"/>
      |  <line x1="12" y1="17" x2="12" y2="21"/>
      |</svg>""".stripMargin

  /** Globe — Geography. */
  val globe: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <circle cx="12" cy="12" r="10"/>
      |  <line x1="2" y1="12" x2="22" y2="12"/>
      |  <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
      |</svg>""".stripMargin

  /** Landmark — History & Politics. */
  val landmark: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <line x1="3" y1="22" x2="21" y2="22"/>
      |  <line x1="6" y1="18" x2="6" y2="11"/>
      |  <line x1="10" y1="18" x2="10" y2="11"/>
      |  <line x1="14" y1="18" x2="14" y2="11"/>
      |  <line x1="18" y1="18" x2="18" y2="11"/>
      |  <polygon points="12 2 20 7 4 7"/>
      |  <line x1="2" y1="18" x2="22" y2="18"/>
      |</svg>""".stripMargin

  /** Church/temple — Religion & Culture. */
  val temple: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <path d="M12 2L8 6h8l-4-4Z"/>
      |  <path d="M8 6v4l-4 2v8h16v-8l-4-2V6"/>
      |  <line x1="12" y1="2" x2="12" y2="6"/>
      |  <rect x="10" y="14" width="4" height="6"/>
      |</svg>""".stripMargin

  /** Question mark — Unknown category. */
  val questionMark: String =
    """<svg viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      |  <circle cx="12" cy="12" r="10"/>
      |  <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/>
      |  <line x1="12" y1="17" x2="12.01" y2="17"/>
      |</svg>""".stripMargin

  /** Get category icon SVG string by category code. */
  def categoryIcon(code: String): String = code match {
    case "GEN" => brain
    case "SPO" => football
    case "MUS" => music
    case "MAT" => hash
    case "LAN" => book
    case "TEC" => monitor
    case "GEO" => globe
    case "HIS" => landmark
    case "REC" => temple
    case _     => questionMark
  }

  /** Winner avatar — larger stickman with glow filter. */
  def winnerAvatar(headColor: String, bodyColor: String): String =
    s"""<svg viewBox="0 0 60 80" class="winner-avatar">
       |  <defs>
       |    <filter id="winner-glow" x="-50%" y="-50%" width="200%" height="200%">
       |      <feGaussianBlur stdDeviation="3" result="blur"/>
       |      <feFlood flood-color="#c3fc02" flood-opacity="0.5" result="color"/>
       |      <feComposite in="color" in2="blur" operator="in" result="shadow"/>
       |      <feMerge><feMergeNode in="shadow"/><feMergeNode in="SourceGraphic"/></feMerge>
       |    </filter>
       |  </defs>
       |  <g filter="url(#winner-glow)">
       |    <circle cx="30" cy="18" r="14" fill="$headColor"/>
       |    <rect x="14" y="35" width="32" height="40" rx="8" fill="$bodyColor"/>
       |  </g>
       |</svg>""".stripMargin
}

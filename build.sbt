import Dependencies._

ThisBuild / scalaVersion     := "2.13.18"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    name := "one-percent",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "me.shadaj" %%% "slinky-core" % "0.7.4",
      "me.shadaj" %%% "slinky-web"  % "0.7.4"
    ),
    Compile / npmDependencies ++= Seq(
      "react"     -> "17.0.2",
      "react-dom" -> "17.0.2"
    ),
    scalacOptions += "-Ymacro-annotations",
    webpack / version := "5.75.0",
    startWebpackDevServer / version := "4.11.1",
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    Compile / sourceGenerators += Def.task {
      val csvDir  = baseDirectory.value / "src" / "main" / "scala" / "game" / "csv"
      val outFile = (Compile / sourceManaged).value / "game" / "CsvData.scala"
      val players    = IO.read(csvDir / "players.csv")
      val questions  = IO.read(csvDir / "questions.csv")
      val categories = IO.read(csvDir / "categories.csv")
      def esc(s: String): String = s.replace("\"\"\"", "\\\"\\\"\\\"")
      val src =
        s"""package game
           |
           |/** Auto-generated from CSV files at compile time. Do not edit. */
           |object CsvData {
           |  val playersCSV: String = \"\"\"${esc(players.trim)}\"\"\"
           |  val questionsCSV: String = \"\"\"${esc(questions.trim)}\"\"\"
           |  val categoriesCSV: String = \"\"\"${esc(categories.trim)}\"\"\"
           |}
           |""".stripMargin
      IO.write(outFile, src)
      Seq(outFile)
    }.taskValue
  )

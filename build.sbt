import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
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
    webpackBundlingMode := BundlingMode.LibraryAndApplication()
  )

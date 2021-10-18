name := "EnglishVocabulary"

lazy val reactJsV      = "17.0.2"
lazy val scalaJsReactV = "2.0.0-RC3"
lazy val scalaCssV     = "0.8.0-RC1"
lazy val CirceVersion  = "0.14.1"
lazy val http4sVersion = "0.23.5"
lazy val specs2Version = "5.0.0-RC-15"
lazy val skunkVersion  = "0.2.2"

lazy val projectSettings = Seq(version := "1.0", scalaVersion := "3.0.2")

val webjars: Seq[ModuleID] = Seq("org.webjars" % "bootstrap" % "5.1.2")

val http4sCirce: Seq[ModuleID] = Seq(
  "org.http4s" %% "http4s-circe"  % http4sVersion,
  // Optional for auto-derivation of JSON codecs
  "io.circe"   %% "circe-generic" % CirceVersion)

lazy val common                = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("common"))
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core"    % CirceVersion,
      "io.circe" %% "circe-parser"  % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion))
  .settings(projectSettings: _*)

lazy val `scalajs-client` = (project in file("scalajs-client"))
  .settings(projectSettings: _*)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= Seq(
      "io.github.chronoscala"             %%% "chronoscala"   % "2.0.2",
      "com.github.japgolly.scalajs-react" %%% "core"          % scalaJsReactV,
      "com.github.japgolly.scalajs-react" %%% "extra"         % scalaJsReactV,
      "com.github.japgolly.scalacss"      %%% "ext-react"     % scalaCssV,
      "io.circe"                          %%% "circe-core"    % CirceVersion,
      "io.circe"                          %%% "circe-parser"  % CirceVersion,
      "io.circe"                          %%% "circe-generic" % CirceVersion),
    webpackEmitSourceMaps           := false,
    Compile / npmDependencies ++= Seq("react" -> reactJsV, "react-dom" -> reactJsV))
  .enablePlugins(ScalaJSBundlerPlugin)
  .dependsOn(common.js)

lazy val `server` = project
  .in(file("server"))
  .dependsOn(common.jvm)
  .settings(projectSettings: _*)
  .settings(
    scalaJSProjects         := Seq(`scalajs-client`),
    Assets / pipelineStages := Seq(scalaJSPipeline),
    pipelineStages          := Seq(digest, gzip),
    Compile / compile       := ((Compile / compile) dependsOn scalaJSPipeline).value)
  .settings(
    Global / onChangedBuildSource := IgnoreSourceChanges,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= webjars ++ http4sCirce ++ Seq(
      "org.specs2"    %% "specs2-core"         % specs2Version % Test,
      "org.http4s"    %% "http4s-dsl"          % http4sVersion,
      "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
      "org.tpolecat"  %% "skunk-core"          % skunkVersion,
      "org.typelevel" %% "cats-core"           % "2.6.1",
      "org.typelevel" %% "cats-effect"         % "3.3-162-2022ef9"),
    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-unchecked"))
  .enablePlugins(WebScalaJSBundlerPlugin)

lazy val `english_vocabulary` = (project in file("."))
  .aggregate(`server`, `scalajs-client`)

Global / onLoad := (Global / onLoad).value.andThen(state => "project server" :: state)

name := """anomalydetection"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

libraryDependencies += filters

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.6.2"

libraryDependencies += "com.sksamuel.elastic4s" % "elastic4s-jackson_2.11" % "1.6.5"

libraryDependencies += "org.json4s" % "json4s-jackson_2.10" % "3.3.0.RC3"




resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

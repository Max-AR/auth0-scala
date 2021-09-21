name := """auth0-regular-webapp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"


resolvers ++= Seq(
  Resolver.typesafeIvyRepo("releases"),
  Resolver.typesafeRepo("releases")
)

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  guice
)


fork in run := true
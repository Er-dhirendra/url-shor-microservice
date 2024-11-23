import sbt._

object Dependencies {
  val akkaVersion = "2.8.8"
  val akkaHttpVersion = "10.5.3"
  val scalacticVersion = "3.2.19"
  val scalatestVersion = "3.2.19"
  val mockitoVersion = "3.2.10.0"

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  )

  val test = Seq(
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    "org.scalatestplus" %% "mockito-3-4" % mockitoVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
  )

  // Combine all dependencies
  val all: Seq[sbt.ModuleID] = akka ++ test
}

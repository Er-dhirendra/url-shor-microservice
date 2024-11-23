ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "url-shor-microservice",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.8",
      "com.typesafe.akka" %% "akka-stream-typed" % "2.8.8",
      "com.typesafe.akka" %% "akka-http" % "10.5.3",
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.8" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.5.3" % Test

    )
  )

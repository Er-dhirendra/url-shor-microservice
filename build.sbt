ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "url-shor-microservice",
    libraryDependencies ++=Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.8",
       "com.typesafe.akka" %% "akka-stream-typed" % "2.8.8",
     "com.typesafe.akka" %% "akka-http" % "10.5.3",


"com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.8" % Test


    )
  )

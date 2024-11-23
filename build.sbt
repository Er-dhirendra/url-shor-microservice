// General build settings
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.4"

// Root project settings
lazy val root = (project in file("."))
  .settings(
    name := "url-shor-microservice",
    libraryDependencies ++= Dependencies.all
  )

// Code coverage settings
coverageEnabled := true

// Excluding files from coverage
coverageExcludedFiles := ".*(?i)(url/app/).*"

// Coverage thresholds
coverageFailOnMinimum := true
coverageMinimumStmtTotal := 90
coverageMinimumBranchTotal := 90
coverageMinimumStmtPerPackage := 90
coverageMinimumBranchPerPackage := 90
coverageMinimumStmtPerFile := 90
coverageMinimumBranchPerFile := 90

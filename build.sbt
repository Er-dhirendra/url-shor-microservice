// General build settings
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.4"

// Root project settings
lazy val root = (project in file("."))
  .settings(
    name := "url-short-microservice",
    // Set the main class for the assembly plugin (fat JAR)
    assembly / mainClass := Some("url.app.UrlShortApplication"),
    // Enable automatic Scala formatting on each compile (optional)
    scalafmtOnCompile := true,
    // Link to the ScalaFmt config file
    scalafmtConfig := file(".scalafmt.conf"),
    // Add project dependencies (ensure this is correct in your Dependencies object)
    libraryDependencies ++= Dependencies.all
  )

// Enable necessary plugins
enablePlugins(DockerPlugin, ScalafmtPlugin)

// Code coverage settings
coverageEnabled := true

// Exclude files/folders from code coverage (specific to your package structure)
coverageExcludedFiles := ".*(?i)(url/app/).*"

// Coverage thresholds (can be adjusted based on your needs)
coverageFailOnMinimum := true
coverageMinimumStmtTotal := 90
coverageMinimumBranchTotal := 90
coverageMinimumStmtPerPackage := 90
coverageMinimumBranchPerPackage := 90
coverageMinimumStmtPerFile := 90
coverageMinimumBranchPerFile := 90

// Dockerfile configuration for the project
docker / dockerfile := {
  val jarFile: File = (assembly / assemblyOutputPath).value // Correct path to fat JAR
  val mainclass = (assembly / mainClass).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"

  new Dockerfile {
    // Use an appropriate base image for Java (OpenJDK 11 for better performance/security)
    from("openjdk:11-jre")

    // Add the fat JAR file to the Docker container
    add(jarFile, jarTarget)

    // Expose port 8080 to the outside world
    expose(8080)

    // Set the entry point for running the JAR file
    entryPoint("java", "-jar", jarTarget)
  }
}

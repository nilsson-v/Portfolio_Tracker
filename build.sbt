ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "Data-Management-Visualisation"
  )
libraryDependencies += "org.scalafx" % "scalafx_3" % "20.0.0-R31"

libraryDependencies += "com.lihaoyi" %% "upickle" % "3.2.0"

libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.9.1"



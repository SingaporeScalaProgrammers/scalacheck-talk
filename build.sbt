name := "scalacheck-talk"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)
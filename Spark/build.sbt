import sbt._
import Keys._
import sbtassembly._
import sbtassembly.MergeStrategy
import sbtassembly.PathList

name := "Spark"

version := "1.0"

scalaVersion := "2.10.5"

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList("org", "apache", xs@_*) => MergeStrategy.first
  case "about.html" => MergeStrategy.rename
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.last
}


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.2" % "provided",
  "com.datastax.spark" % "spark-cassandra-connector_2.10" % "1.6.0-M2",
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1",
  "com.sparkjava" % "spark-core" % "2.5",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "com.google.code.gson" % "gson" % "2.7"
)
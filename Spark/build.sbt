name := "Spark"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.1",
  "com.datastax.spark" % "spark-cassandra-connector_2.10" % "1.6.0-M2",
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1",
  "com.sparkjava" % "spark-core" % "2.5",
  "org.slf4j" % "slf4j-simple" % "1.7.21"
)
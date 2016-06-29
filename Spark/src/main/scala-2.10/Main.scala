import Spark.SparkManager
import Spark.SparkServer

import scala.collection.mutable.ListBuffer

object Main {

  def main(args: Array[String]): Unit = {
    SparkManager.buildConfigureSpark()
    SparkServer.configureServer()
  }
}
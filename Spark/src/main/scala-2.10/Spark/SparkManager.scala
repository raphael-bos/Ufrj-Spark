package Spark

import org.apache.spark.{SparkConf, SparkContext}

object SparkManager {

  var context : SparkContext = null
  var instance = false
  private var sparkConf:SparkConf = null

  def buildConfigureSpark() = {
    sparkConf = new SparkConf().setAppName("ProjetoSpark").setMaster("local[*]").set("spark.cassandra.connection.host", "127.0.0.1")
    context = new SparkContext(sparkConf)
  }
  def configureSpark() : SparkContext = {
    if(context == null && sparkConf != null){
      context = new SparkContext(sparkConf)
    }
    context
  }

  lazy val sparkContext : SparkContext = context

}

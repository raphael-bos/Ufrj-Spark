/**
  * Created by raphael on 22/06/16.
  */

import Spark.SparkManager._
import utils.CassandraLoader
import utils.Downloader

object Test {

  def main(args: Array[String]): Unit = {
    buildConfigureSpark()
    CassandraLoader.frequencia_e_aprovacao()
  }
}

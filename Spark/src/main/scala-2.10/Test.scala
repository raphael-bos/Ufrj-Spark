/**
  * Created by raphael on 22/06/16.
  */

import Spark.SparkManager._
import utils.CassandraLoader
import utils.Downloader
import utils.UsefulData
import com.datastax.spark.connector._
import schemas.Cassandra._

object Test {

  def main(args: Array[String]): Unit = {
    buildConfigureSpark()
    val set = sparkContext.cassandraTable[Media]("spark", "media")
    val result1 = set.map(x => (x.ordem_materia, (x.ordem_materia, x.descricao_materia))).distinct().collect().toMap
    val result2 = materiasMap.toArray.map(x => result1.getOrElse[(Int,String)](x._1,x))
    val lala = result2.sortBy(x => x._2).map(x => "{cod: "+ x._1 + ", descr: "+ x._2 +"}").mkString(",\n")
    lala
  }
}

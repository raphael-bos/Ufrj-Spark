package analyser

/**
  * Created by raphael on 20/07/16.
  */

import org.apache.spark.rdd.RDD
import schemas.Cassandra.Escola
import schemas.Filter._
import Spark.SparkManager._
import com.datastax.spark.connector._

object FilterMethods {

  def getEscolas(myfilter: BasicFilter): RDD[Escola] ={

    val result = sparkContext.cassandraTable[Escola]("spark","escolas")
      .filter(x => myfilter.bairros.contains(x.bairro.toUpperCase()))

    result
  }

}

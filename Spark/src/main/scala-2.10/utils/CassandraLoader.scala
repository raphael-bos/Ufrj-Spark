package utils

import Spark.SparkManager.sparkContext
import schemas.Cassandra.Escola
import com.datastax.spark.connector._

/**
  * Created by raphael on 22/06/16.
  */
object CassandraLoader {

  def escolas(): Unit ={

    val basePath = "/home/raphael/Documents/Projetos/GitProj/DataRio/"
    val fileName = "escolas__.csv"

    val text = sparkContext.textFile(basePath + fileName)
    val lista = text.map(x => x.split(","))
      .filter(x => x.length > 18)
      .filter(x =>{
        try{
          x.head.toInt
          true
        }
        catch{
          case e: Exception => false
        }
      })
      .map(x => x:+ "")
      .map(x => new Escola(x(0), x(1), x(2), x(3), x(4), x(5), x(6), x(7), x(8),
        x(9), x(10), x(11), x(12), x(13), x(14), x(15),x(16), x(17), x(18), x(19)))

    lista.saveToCassandra("spark","escolas")
  }



}

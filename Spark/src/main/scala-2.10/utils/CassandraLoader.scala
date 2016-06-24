package utils

import Spark.SparkManager.sparkContext
import schemas.Cassandra.{Escola, Frequencia_e_Aprovacao, Professores}
import com.datastax.spark.connector._

/**
  * Created by raphael on 22/06/16.
  */
object CassandraLoader {

  val basePath = "/home/raphael/Documents/Projetos/GitProj/DataRio/"

  def escolas(): Unit ={

    val fileName = "escolas__.csv"

    val text = sparkContext.textFile(basePath + fileName)
    val lista = text.map(line => line.split(","))
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

  def professores(): Unit ={

    val filename = "ProfessoresEscola.csv"
    val text = sparkContext.textFile(basePath + filename)
    val lista = text.map(line => line.split(","))
        .filter(x => x.length > 1)
        .filter(x => x(0) != "Nome")
        .map(x => {
          if(x(1).length == 6)
            x(1) = "0"+x(1)
          x
        })
      .map(x => new Professores(x(1), x(3).toInt, x(4).toInt, x(5).toInt, x(6).toInt, x(7).toInt,
        x(8).toInt, x(9).toInt, x(10).toInt, x(11).toInt, x(12).toInt, x(13).toInt, x(14).toInt,
        x(15).toInt, x(16).toInt, x(17).toInt, x(18).toInt, x(19).toInt, x(20).toInt, x(21).toInt, x(22).toInt))

   lista.saveToCassandra("spark","professores")

  }

  def frequencia_e_aprovacao(): Unit ={
    val path = basePath + "Frequencia/"
    val listaId = sparkContext.cassandraTable[Escola]("spark","escolas").map(x => x.designacao)

    listaId.foreach(id =>{
      val text = sparkContext.textFile(path + id + ".csv")
      val lista =text.map(x => x.split(","))
        .filter(x => x.length > 1)
        .filter(x => x(0) != "Ano Letivo")
        .filter(x => x(3) != "Total")
        .map(x =>{
          if(x(2) == "")
            x(2) = "0"
          if(x(4) == "")
            x(4) = "0"
          if(x(5) == "")
            x(5) = "0"
          if(x(6) == "")
            x(6) = "0"
          x
        })
        .map(x => new Frequencia_e_Aprovacao(id, x(0), x(2).toInt, x(3), x(4).toInt, x(5).toInt, x(6).toInt))

      lista.saveToCassandra("spark","frequencia_e_aprovacao")
    })
  }


}

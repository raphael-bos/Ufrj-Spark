package utils

import Spark.SparkManager.sparkContext
import schemas.Cassandra._
import com.datastax.spark.connector._
import org.apache.spark.rdd.RDD

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

  def professores2(): Unit={

    val filename = "ProfessoresEscola.csv"
    val text = sparkContext.textFile(basePath + filename)
    val lista: RDD[Professores2]= text.map(line => line.split(","))
      .filter(x => x.length > 1)
      .filter(x => x(0) != "Nome")
      .map(x => {
        if(x(1).length == 6)
          x(1) = "0"+x(1)
        x
      })
      .flatMap(x => Array[Professores2](
        new Professores2(x(1), 15, materiasMap(15), x(3).toInt),
        new Professores2(x(1), 16, materiasMap(16), x(4).toInt),
        new Professores2(x(1), 18, materiasMap(18), x(5).toInt),
        new Professores2(x(1), 1, materiasMap(1), x(6).toInt),
        new Professores2(x(1), 10, materiasMap(10), x(7).toInt),
        new Professores2(x(1), 8, materiasMap(8), x(8).toInt),
        new Professores2(x(1), 17, materiasMap(17), x(9).toInt),
        new Professores2(x(1), 11, materiasMap(11), x(10).toInt),
        new Professores2(x(1), 12, materiasMap(12), x(11).toInt),
        new Professores2(x(1), 2, materiasMap(2), x(12).toInt),
        new Professores2(x(1), 3, materiasMap(3), x(13).toInt),
        new Professores2(x(1), 13, materiasMap(13), x(14).toInt),
        new Professores2(x(1), 5, materiasMap(5), x(15).toInt),
        new Professores2(x(1), 4, materiasMap(4), x(16).toInt),
        new Professores2(x(1), 14, materiasMap(14), x(17).toInt),
        new Professores2(x(1), 19, materiasMap(19), x(18).toInt),
        new Professores2(x(1), 20, materiasMap(20), x(19).toInt),
        new Professores2(x(1), 23, materiasMap(23), x(20).toInt),
        new Professores2(x(1), 24, materiasMap(24), x(21).toInt),
        new Professores2(x(1), 25, materiasMap(25), x(22).toInt)
      ))

    lista.saveToCassandra("spark","professores2")
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

  def media(): Unit ={
    val path = basePath + "Media/"
    val listaId = sparkContext.cassandraTable[Escola]("spark","escolas").map(x => x.designacao)
    listaId.foreach(id => {
      val text = sparkContext.textFile(path + id + ".csv")
      val lista = text.map(x => x.split(","))
        .filter(x => x.length == 11)
        .filter(x => x(0) != "Ano Letivo")
        .map(x => new Media(id, x(0), x(3).toInt, x(4).toInt, x(5).toInt, x(7).toInt, x(6), x(8), x(9), x(10).toDouble))

      lista.saveToCassandra("spark","media")
    })
  }

}

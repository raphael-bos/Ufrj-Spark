package utils

/**
  * Created by raphael on 26/06/16.
  */

import Spark.SparkManager.sparkContext
import schemas.Cassandra.{Escola, Frequencia_e_Aprovacao, Media, Professores}
import com.datastax.spark.connector._

object UsefulData {

  def codigosMateria(): Array[(Int,String,String)] ={
    val data = sparkContext.cassandraTable[Media]("spark","media")

    val resultado = data.groupBy(x  => x.ordem_materia).map(x => x._2.head).map(x => (x.ordem_materia,x.codigo_materia,x.descricao_materia)).collect()
    resultado
  }
}

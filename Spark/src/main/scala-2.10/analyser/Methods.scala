package analyser

import com.datastax.spark.connector._
import Spark.SparkManager._
import schemas.Cassandra._

object Methods {

  def buscarProfessores(): Array[Professores] ={
    val professores = sparkContext.cassandraTable[Professores]("spark","professores")
    val retorno = professores.map(x => (x.ordem_materia,x.quantidade))
        .filter(x => x._1 != 25) //Retirar professor 2
        .reduceByKey((acc, x) => acc + x)
        .map(x => new Professores("", x._1 , materiasMap(x._1), x._2))
        .collect()
    retorno
  }
}

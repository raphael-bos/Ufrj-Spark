package analyser

import com.datastax.spark.connector._
import Spark.SparkManager._
import schemas.Cassandra._
import Utils._

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

  def buscarMedias(): Array[Media] ={
    val medias = sparkContext.parallelize(sparkContext.cassandraTable[Media]("spark","media").collect())
    val retorno = medias.filter(x => x.ordem_materia == 4)  //Selecionar matematica apenas
      .map(x => ((x.ano,x.bimestre),(x.media, 1))) //Agrupar por ano e bimestre
      .reduceByKey((acc, x) => {
        (acc._1 + x._1, acc._2 + x._2)
      })
      .map(x => (x._1,x._2._1/x._2._2)) //Media das medias
      .map(x => new Media("",x._1._1,x._1._2,0,0,4,materiasMap(4),"","",x._2))
      .collect()
    retorno
  }

  def buscarAprovacao(): Array[EscolaInfo] ={
    val aprovacao = sparkContext.cassandraTable[Frequencia_e_Aprovacao]("spark","frequencia_e_aprovacao")
    val melhoresAprovacoes = aprovacao.map(x => (x.designacao,(x.aprovados,x.avaliados)))
        .reduceByKey((acc, x) => (acc._1 + x._1, acc._2 + x._2))
        .map(x => (x._1,(x._2._1.toDouble/x._2._2.toDouble)*100))
      .map(x =>{
      val arr = new Array[(String, Double)](5)
      arr(0) = x
      arr
    }).reduce((acc, x) => mergeByMost(acc,x,5))

    val listaDesignacao = melhoresAprovacoes.map(x => x._1).mkString("','")
    val escolas = sparkContext.cassandraTable[Escola]("spark","escolas").where("designacao in ('" + listaDesignacao + "')")
    //val teste = escolas.colletct()
    val escolasMap = melhoresAprovacoes.toMap
    val retorno = escolas.map(x => new EscolaInfo(x.designacao,x.nome,x.bairro, escolasMap(x.designacao))).collect()
    retorno
  }

  def buscarFrequencia(): Array[EscolaInfo] ={
    new Array[EscolaInfo](4)
  }

}

package analyser

import com.datastax.spark.connector._
import Spark.SparkManager._
import schemas.Cassandra._
import Utils._
import schemas.DataObject.EvolucaoMedia
import schemas.Filter.BasicFilter

import scala.collection.mutable.ListBuffer

object Methods {

  def buscarProfessores(filter: BasicFilter): Array[Professores] ={
    val escolas = FilterMethods.getEscolas(filter)
    val listaIds = escolas.map(x => (x.designacao,x))

    val professores = sparkContext.cassandraTable[Professores]("spark","professores")
      .map(x => (x.designacao,x))
      .join(listaIds) //FiltroEscolas
      .map(x => x._2._1)
      .filter(x => filter.disciplinas.contains(x.ordem_materia)) //Filtro Materia

    val retorno = professores.map(x => (x.ordem_materia,x.quantidade))
        .reduceByKey((acc, x) => acc + x)
        .map(x => new Professores("", x._1 , materiasMap(x._1), x._2))
        .collect()
    retorno
  }

  def buscarMedias(filter: BasicFilter): EvolucaoMedia ={
    val escolas = FilterMethods.getEscolas(filter)//.collect()
    val listaIds = escolas.map(x => (x.designacao,x))
    val medias = sparkContext.cassandraTable[Media]("spark","media")
        .map(x => (x.designacao,x))
        .join(listaIds) //FiltroEscolas
        .map(x => x._2._1)

    val dadosMedia = medias.filter(x =>  filter.anos.contains(x.ano))
      .filter(x => filter.turmas.contains(x.serie))
      .filter(x => filter.disciplinas.contains(x.ordem_materia))
      .map(x => ((x.ano,x.bimestre,x.ordem_materia,x.serie, x.descricao_serie),(x.media, 1))) //Agrupar por ano e bimestre
      .reduceByKey((acc, x) => {
        (acc._1 + x._1, acc._2 + x._2)
      })
      .map(x => (x._1,x._2._1/x._2._2))
      .map(x => new Media("",x._1._1,x._1._2,0,x._1._4,x._1._3,x._1._5,"", materiasMap(x._1._3),x._2))
      .collect()

    val mediaByKey = dadosMedia.map(x => ((x.descricao_materia,x.descricao_serie,x.ano,x.bimestre),x.media)).toMap
    val tempos = dadosMedia.map(x => (x.ano,x.bimestre)).distinct.sortWith((y,x) => {
      if(y._1 != x._1)
        y._1 < x._1
      y._2 < x._2
    })
    val materias = dadosMedia.map(x => (x.descricao_materia, x.descricao_serie)).distinct.sorted

    val arrayMedias = new Array[Array[Double]](tempos.length)
    for(i <- arrayMedias.indices){
      arrayMedias(i) = new Array(materias.length)
      for(j <- arrayMedias(i).indices){
        arrayMedias(i)(j) = mediaByKey(materias(j)._1,materias(j)._2,tempos(i)._1,tempos(i)._2)
      }
    }

    val temposFormatados = tempos.map(x => x._1 + "/" + x._2)
    val disciplinasFormatadas = materias.map(x => x._1 + " - " + x._2)
    val retorno = new EvolucaoMedia(disciplinasFormatadas,temposFormatados,arrayMedias)
    retorno
  }

  def buscarAprovacao(filter: BasicFilter): Array[EscolaInfo] ={
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

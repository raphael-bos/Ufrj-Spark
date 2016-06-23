package utils

/**
  * Created by raphael on 22/06/16.
  */


import sys.process._
import java.net.URL
import java.io.File

import Spark.SparkManager.sparkContext
import schemas.Cassandra.Escola
import com.datastax.spark.connector._

object Downloader {

  lazy val lista = sparkContext.cassandraTable[Escola]("spark","escolas").map(x => x.designacao).collect()
  private val basePath = "/home/raphael/Documents/Projetos/GitProj/DataRio/"

  def mediaEscolar(): Unit ={
    lista.foreach(x =>{
      fileDownloader(mediaURL(x),basePath + "Media/" + x.toString + ".csv")
    })
  }

  def frequenciaEscolar(): Unit ={
    lista.foreach(x =>{
      fileDownloader(frequenciaURL(x),basePath + "Frequencia/" + x.toString + ".csv")
    })
  }

  private def fileDownloader(url: String, filename: String) = {
    new URL(url) #> new File(filename) !!
  }

  private def mediaURL(des: String): String = {
    val res = "http://dadosabertos.rio.rj.gov.br/apiEducacao/apresentacao/csv/mediasEscolaPorAno.cfm?designacao=" + des + "&formato=ckan"
    res
  }

  private def frequenciaURL(des: String): String = {
    val res = "http://dadosabertos.rio.rj.gov.br/apiEducacao/apresentacao/csv/freqMediaIndAprovEscolaPorAno.cfm?designacao=" + des + "&formato=ckan"
    res
  }

}

package schemas

/**
  * Created by raphael on 20/07/16.
  */
object DataObject {

  case class EvolucaoMedia(disciplinas: Array[String], tempos: Array[String] ,medias: Array[Array[Double]])

  case class AprovacaoEscolas(aprovacao: Array[(String,Double)])

}

package schemas

/**
  * Created by raphael on 20/07/16.
  */
object Filter {

  case class BasicFilter(bairros: Array[String], disciplinas: Array[Int], anos: Array[String], turmas: Array[Int])

}

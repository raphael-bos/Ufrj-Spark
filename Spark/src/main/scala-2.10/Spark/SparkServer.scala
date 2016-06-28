package Spark{

  import spark.{Request, Response, Route}
  import spark.Spark._

  import scala.collection.mutable.ListBuffer
  //import org.json4s._
  //import org.json4s.native.JsonMethods._
  //import org.json4s.JsonDSL._
  import com.google.gson._

  object SparkServer {

    case class obj(Id: Int)
    case class dados(Nome: String, Telefone: Int)

    def main(args: Array[String]) {

      port(8080)

      val gson = new Gson()

      staticFiles.location("/public")

      get("hello", new Route {
        def handle(request: Request, response: Response): AnyRef = {
          "Hello"
        }
      })

      post("Index.html/Metodo", new Route {
        def handle(request: Request, response: Response): AnyRef ={
          val objeto = gson.fromJson(request.body(), classOf[obj])
          val retorno = gson.toJson(Array(dados("Nome1",20),dados("Nome2",30)))
          retorno
        }
      })

      redirect.get("/", "Index.html")

    }
  }

}


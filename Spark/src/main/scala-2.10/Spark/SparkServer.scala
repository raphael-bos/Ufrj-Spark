package Spark{

  import spark.{Request, Response, Route}
  import spark.Spark._
  import Spark.SparkManager._
  import analyser.Methods._
  import schemas.Cassandra.Professores

  object SparkServer {

    case class obj(Id: Int)
    case class dados(Nome: String, Telefone: Int)

    def configureServer(): Unit ={
      port(8000)
      staticFiles.location("/public")
      configureMethods()
      redirect.get("/", "Index.html")
    }

    def configureMethods(): Unit ={

      post("Index.html/Metodo", new Route {
        def handle(request: Request, response: Response): AnyRef ={
          val objeto = gson.fromJson(request.body(), classOf[obj])
          val retorno = gson.toJson(Array(dados("Nome1",20),dados("Nome2",30)))
          retorno
        }
      })

      post("Index.html/professores", new Route {
        def handle(request: Request, response: Response): AnyRef = {
          val resposta: Array[Professores] = buscarProfessores()
          gson.toJson(resposta)
        }
      })

    }

  }

}


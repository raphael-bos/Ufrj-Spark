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

      post("Index.html/professores", new Route {
        def handle(request: Request, response: Response): AnyRef = {
          val resposta: Array[Professores] = buscarProfessores()
          gson.toJson(resposta)
        }
      })

      post("Index.html/medias", new Route {
        def handle(request: Request, response: Response): AnyRef = {
          val resposta = buscarMedias()
          gson.toJson(resposta)
        }
      })

      post("Index.html/aprovacao", new Route {
        def handle(request: Request, response: Response): AnyRef = {
          val resposta = buscarAprovacao()
          gson.toJson(resposta)
        }
      })

    }

  }

}


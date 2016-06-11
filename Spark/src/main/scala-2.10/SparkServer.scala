package sparkServer.http

import org.apache.log4j.BasicConfigurator
import spark.Spark._
import spark._

object ServerSpark {

  def main(args: Array[String]) {
    //BasicConfigurator.configure()
    port(8080)
    get("hello", new Route {
      def handle(request: Request, response: Response): AnyRef = {
        dataAnalyzer.Main.main(null)
        "Hello!"
      }
    })
  }
}
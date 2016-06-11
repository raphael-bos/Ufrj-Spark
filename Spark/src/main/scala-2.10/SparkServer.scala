import spark.{Request, Response, Route}
import spark.Spark._

object SparkServer{

  def main(args: Array[String]) {
    port(8080)
    get("hello", new Route {
      def handle(request: Request, response: Response): AnyRef = {
        "Hello!"
      }
    })
  }

}

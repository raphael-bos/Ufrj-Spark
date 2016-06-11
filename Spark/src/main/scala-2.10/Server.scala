package spark.httpServer

import java.io.{InputStream, OutputStream}
import java.net.InetSocketAddress
import scala.io.Source


import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}


object Server {

  def main(args: Array[String]) {
    val server = HttpServer.create(new InetSocketAddress(8000), 0)
    server.createContext("/", new RootHandler())
    server.setExecutor(null)

    server.start()

    println("Qualquer tecla para sair...")

    System.in.read()
    server.stop(0)
  }

}

class RootHandler extends HttpHandler {

  def handle(t: HttpExchange) {
    displayPayload(t.getRequestBody)
    sendResponse(t)
  }

  private def displayPayload(body: InputStream): Unit ={
    println()
    println("******************** REQUEST START ********************")
    println()
    copyStream(body, System.out)
    println()
    println("********************* REQUEST END *********************")
    println()
  }

  private def copyStream(in: InputStream, out: OutputStream) {
    Iterator
      .continually(in.read)
      .takeWhile(-1 !=)
      .foreach(out.write)
  }

  private def sendResponse(t: HttpExchange) {
    val response = Source.fromFile("src/main/web/view/Index.html").mkString
    //val response = "Ack!"
    t.sendResponseHeaders(200, response.length())
    val os = t.getResponseBody
    os.write(response.getBytes)
    os.close()
  }

}
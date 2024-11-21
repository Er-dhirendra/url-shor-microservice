package url.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.http.scaladsl.Http
import url.controller.UrlShortenController
import url.repositry.InMemoryRepositry
import url.service.{ Base64UrlShortingStrategy, UrlShortiningService }

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Application extends App {

  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "Application")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  val repositry = new InMemoryRepositry
  val base64UrlShortingStrategy = new Base64UrlShortingStrategy
  val service = new UrlShortiningService(repositry, base64UrlShortingStrategy)
  val controller = new UrlShortenController(service)
  val bindingFuture = Http().newServerAt("localhost", 8080).bind(controller.route)
  println(s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}

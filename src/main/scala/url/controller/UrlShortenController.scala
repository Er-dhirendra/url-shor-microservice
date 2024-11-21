package url.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import url.service.UrlShortiningService

class UrlShortenController(service: UrlShortiningService) {
  val route: Route = path("shorten") {
      post {
        entity(as[String]) { originalUrl =>
          val shortUrl = service.shortenUrl(originalUrl)
          complete(shortUrl)
        }
      }
    } ~ path("resolve" / Segment) { shortUrl =>
      service.resolverUrl(shortUrl) match {
        case Some(value) => complete(value)
        case None        => complete(StatusCodes.NotFound, "Shorten Url Not Found")

      }
    }
}

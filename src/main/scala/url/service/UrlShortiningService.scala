package url.service
import url.domin.{ Repositry, UrlMapping }

import java.nio.charset.StandardCharsets
import java.util.Base64

trait UrlShortingStrategy {
  def encode(originalUrl: String): String
}

class Base64UrlShortingStrategy extends UrlShortingStrategy {
  override def encode(originalUrl: String): String =
    Base64.getEncoder.encodeToString(originalUrl.getBytes(StandardCharsets.UTF_8))
}

class HashUrlShortingStrategy extends UrlShortingStrategy {
  override def encode(originalUrl: String): String = originalUrl.hashCode.toString
}

class UrlShortiningService(repositry: Repositry, shortingUrlStrategy: UrlShortingStrategy) {

  def shortenUrl(originalUrl: String): String = {
    repositry.findByOriginalUrl(originalUrl) match {
      case Some(value) => value.shortUrl
      case None =>
        val shortUrl = shortingUrlStrategy.encode(originalUrl)
        val urlMapping = UrlMapping(shortUrl, originalUrl, System.currentTimeMillis())
        repositry.saveUrl(urlMapping)
        urlMapping.shortUrl
    }
  }

  def resolverUrl(short: String): Option[String] = {
    repositry.findByShortUrl(short).map(_.originalUrl)
  }
}

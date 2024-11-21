package url.repositry

import url.domin.{ Repositry, UrlMapping }

import scala.collection.concurrent.TrieMap

class InMemoryRepositry extends Repositry {

  private val originalToShortenUrlMap = TrieMap.empty[String, UrlMapping]
  private val shortenToOriginalMap = TrieMap.empty[String, UrlMapping]

  override def saveUrl(urlMapping: UrlMapping): Unit = {
    originalToShortenUrlMap.addOne(urlMapping.originalUrl, urlMapping)
    shortenToOriginalMap.addOne(urlMapping.shortUrl, urlMapping)
  }

  override def findByOriginalUrl(originalUrl: String): Option[UrlMapping] = {
    originalToShortenUrlMap.get(originalUrl)
  }

  override def findByShortUrl(shortUrl: String): Option[UrlMapping] = {
    shortenToOriginalMap.get(shortUrl)
  }
}

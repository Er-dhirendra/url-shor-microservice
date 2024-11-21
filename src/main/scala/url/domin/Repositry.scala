package url.domin

case class UrlMapping(shortUrl: String, originalUrl: String, createdDate: Long)
trait Repositry {
  def saveUrl(urlMapping: UrlMapping): Unit
  def findByOriginalUrl(originalUrl: String): Option[UrlMapping]
  def findByShortUrl(shortUrl: String): Option[UrlMapping]
}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import url.domin.{Repositry, UrlMapping}
import url.repositry.InMemoryRepositry

import scala.collection.concurrent.TrieMap

class InMemoryRepositrySpec extends AnyFlatSpec with Matchers {

  val repository: Repositry = new InMemoryRepositry

  "saveUrl" should "save a url mapping to the repository" in {
    val urlMapping = UrlMapping("short123", "http://example.com", System.currentTimeMillis())
    repository.saveUrl(urlMapping)

    // Verifying that the url is saved correctly
    repository.findByOriginalUrl("http://example.com") shouldBe Some(urlMapping)
    repository.findByShortUrl("short123") shouldBe Some(urlMapping)
  }

  "findByOriginalUrl" should "return a url mapping for the given original url" in {
    val urlMapping = UrlMapping("short123", "http://example.com", System.currentTimeMillis())
    repository.saveUrl(urlMapping)

    repository.findByOriginalUrl("http://example.com") shouldBe Some(urlMapping)
  }

  it should "return None if the original url is not found" in {
    repository.findByOriginalUrl("http://nonexistent.com") shouldBe None
  }

  "findByShortUrl" should "return a url mapping for the given short url" in {
    val urlMapping = UrlMapping("short123", "http://example.com", System.currentTimeMillis())
    repository.saveUrl(urlMapping)

    repository.findByShortUrl("short123") shouldBe Some(urlMapping)
  }

  it should "return None if the short url is not found" in {
    repository.findByShortUrl("nonexistent") shouldBe None
  }
}


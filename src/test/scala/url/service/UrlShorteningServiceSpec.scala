package url.service

import org.mockito.Mockito.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import url.domin.{Repositry, UrlMapping}

class UrlShorteningServiceSpec extends AnyFlatSpec with Matchers {

  // Mocking the Repository
  val mockRepository: Repositry = mock[Repositry]

  // Mocking the Shortening Strategy (both Base64 and Hash strategy)
  val base64Strategy: UrlShortingStrategy = new Base64UrlShortingStrategy
  val hashStrategy: UrlShortingStrategy = new HashUrlShortingStrategy

  // Instantiating the service with the mocked repository and a shorting strategy
  val urlShorteningServiceBase64 = new UrlShortiningService(mockRepository, base64Strategy)
  val urlShorteningServiceHash = new UrlShortiningService(mockRepository, hashStrategy)
  val originalUrl = "http://example.com"
  val base64Url = "aHR0cDovL2V4YW1wbGUuY29t"
  val hasUrl = "-631280213"

  "UrlShorteningService" should "return the existing short URL if the original URL is already shortened" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mocking the repository to return the existing mapping
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(Some(urlMapping))

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceBase64.shortenUrl(originalUrl)
    result shouldBe base64Url

    // Verify that the repository method was called
    verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
  }
  it should "use Hash strategy to shorten the URL if provided with HashUrlShortingStrategy" in {
    val urlMapping = UrlMapping(hasUrl, originalUrl, System.currentTimeMillis())
    // Mocking the repository to return None (URL not found)
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(Some(urlMapping))

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceHash.shortenUrl(originalUrl)
    result shouldBe hasUrl

    // Verify that the repository methods were called correctly
    verify(mockRepository, times(2)).findByOriginalUrl(originalUrl)
  }

  "resolverUrl" should "resolve the original URL from the shortened URL" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())
    // Mocking the repository to return the URL mapping for the short URL
    when(mockRepository.findByShortUrl(base64Url)).thenReturn(Some(urlMapping))

    // Call resolverUrl and assert the response
    val result = urlShorteningServiceBase64.resolverUrl(base64Url)
    result shouldBe Some(originalUrl)

    // Verify that the repository method was called
    verify(mockRepository, times(1)).findByShortUrl(base64Url)
  }

  it should "return None when resolving a non-existent shortened URL" in {
    val shortUrl = "nonexistent"

    // Mocking the repository to return None (short URL not found)
    when(mockRepository.findByShortUrl(shortUrl)).thenReturn(None)

    // Call resolverUrl and assert the response
    val result = urlShorteningServiceBase64.resolverUrl(shortUrl)
    result shouldBe None

    // Verify that the repository method was called
    verify(mockRepository, times(1)).findByShortUrl(shortUrl)
  }
  it should "return orginal URL when resolving with shortUrl URL" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mocking the repository to return None (URL not found)
    when(mockRepository.findByShortUrl(base64Url)).thenReturn(Some(urlMapping))

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceBase64.resolverUrl(base64Url)
    result shouldBe Option(originalUrl)

    // Verify that the repository methods were called correctly
    // Verify findByOriginalUrl was called exactly once
    //verify(mockRepository, times(1)).findByShortUrl(originalUrl)

  }

  it should "shorten the URL by base 64 and save it if the original URL is not already shortened" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mocking the repository to return None (URL not found)
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(None)

    // Mocking the saveUrl method (void method) to do nothing
    doNothing().when(mockRepository).saveUrl(urlMapping)

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceBase64.shortenUrl(originalUrl)
    result shouldBe base64Url

    // Verify that the repository methods were called correctly
    // Verify findByOriginalUrl was called exactly once
    //verify(mockRepository, times(3)).findByOriginalUrl(originalUrl)

    // Verify saveUrl was called exactly once
    // verify(mockRepository, times(1)).saveUrl(urlMapping)
  }

  it should "shorten the URL by Has and save it if the original if URL is not already shortened" in {

    val urlMapping = UrlMapping(hasUrl, originalUrl, System.currentTimeMillis())

    // Mocking the repository to return None (URL not found)
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(None)

    // Mocking the saveUrl method (void method) to do nothing
    doNothing().when(mockRepository).saveUrl(urlMapping)

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceHash.shortenUrl(originalUrl)
    result shouldBe hasUrl

    // Verify that the repository methods were called correctly
    // Verify findByOriginalUrl was called exactly once
    //verify(mockRepository, times(4)).findByOriginalUrl(originalUrl)

    // Verify saveUrl was called exactly once
    // verify(mockRepository, times(1)).saveUrl(urlMapping)
  }
}

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import url.domin.{Repositry, UrlMapping}
import url.service.{Base64UrlShortingStrategy, HashUrlShortingStrategy, UrlShortingStrategy, UrlShortiningService}

class UrlShorteningServiceSpec extends AnyFlatSpec with Matchers with MockitoSugar with BeforeAndAfterEach{

  // Common test data
  val originalUrl = "http://example.com"
  val base64Url = "aHR0cDovL2V4YW1wbGUuY29t"
  val hasUrl = "-631280213"

  // Mock repository
  val mockRepository: Repositry = mock[Repositry]

  // Shortening Strategies
  val base64Strategy: UrlShortingStrategy = new Base64UrlShortingStrategy
  val hashStrategy: UrlShortingStrategy = new HashUrlShortingStrategy

  // Services
  val urlShorteningServiceBase64 = new UrlShortiningService(mockRepository, base64Strategy)
  val urlShorteningServiceHash = new UrlShortiningService(mockRepository, hashStrategy)

  override def beforeEach(): Unit = reset(mockRepository)

  "UrlShorteningService" should "return the existing short URL if the original URL is already shortened" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mock the repository behavior
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(Some(urlMapping))

    // Call the method and assert the result
    val result = urlShorteningServiceBase64.shortenUrl(originalUrl)
    result shouldBe base64Url

    // Verify the repository method was called exactly once
    verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
  }

  it should "use Hash strategy to shorten the URL if provided with HashUrlShortingStrategy" in {
    val urlMapping = UrlMapping(hasUrl, originalUrl, System.currentTimeMillis())

    // Mock repository to return the URL mapping
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(Some(urlMapping))

    // Call the method and assert the result
    val result = urlShorteningServiceHash.shortenUrl(originalUrl)
    result shouldBe hasUrl

    // Verify repository method was called exactly once
    verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
  }

  "resolverUrl" should "resolve the original URL from the shortened URL" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mock repository to return the URL mapping for base64Url
    when(mockRepository.findByShortUrl(base64Url)).thenReturn(Some(urlMapping))

    // Call resolverUrl and assert the response
    val result = urlShorteningServiceBase64.resolverUrl(base64Url)
    result shouldBe Some(originalUrl)

    // Verify that findByShortUrl was called exactly once
    verify(mockRepository, times(1)).findByShortUrl(base64Url)
  }

  it should "return None when resolving a non-existent shortened URL" in {
    val shortUrl = "nonexistent"

    // Mock repository to return None (URL not found)
    when(mockRepository.findByShortUrl(shortUrl)).thenReturn(None)

    // Call resolverUrl and assert the response
    val result = urlShorteningServiceBase64.resolverUrl(shortUrl)
    result shouldBe None

    // Verify repository method was called exactly once
    verify(mockRepository, times(1)).findByShortUrl(shortUrl)
  }

  it should "shorten the URL by base64 and save it if the original URL is not already shortened" in {
    val urlMapping = UrlMapping(base64Url, originalUrl, System.currentTimeMillis())

    // Mock repository to return None (URL not found)
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(None)

    // Mock saveUrl to do nothing
    doNothing().when(mockRepository).saveUrl(urlMapping)

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceBase64.shortenUrl(originalUrl)
    result shouldBe base64Url

    // Verify repository methods were called correctly
    verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
    verify(mockRepository, times(1)).saveUrl(urlMapping)
  }

  it should "shorten the URL by Hash and save it if the original URL is not already shortened" in {
    val urlMapping = UrlMapping(hasUrl, originalUrl, System.currentTimeMillis())

    // Mock repository to return None (URL not found)
    when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(None)

    // Mock saveUrl to do nothing
    doNothing().when(mockRepository).saveUrl(urlMapping)

    // Call shortenUrl and assert the response
    val result = urlShorteningServiceHash.shortenUrl(originalUrl)
    result shouldBe hasUrl

    // Verify repository methods were called correctly
    verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
    verify(mockRepository, times(1)).saveUrl(urlMapping)
  }
}

package url.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Route
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import url.service.UrlShortiningService

class UrlShortenControllerSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  // Mocking the UrlShortiningService
  val mockService: UrlShortiningService = mock[UrlShortiningService]
  val controller = new UrlShortenController(mockService)
  val route: Route = controller.route

  "POST /shorten" should "return the shortened URL when provided with an original URL" in {
    val originalUrl = "http://example.com"
    val shortUrl = "short123"

    // Mock the shortenUrl method to return the expected shortened URL
    when(mockService.shortenUrl(originalUrl)).thenReturn(shortUrl)

    // Send a POST request to the /shorten route with the original URL
    Post("/shorten", originalUrl) ~> route ~> check {
      // Check that the response status is OK and the shortened URL is returned
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe shortUrl
    }
  }

  "GET /resolve" should "return the original URL when provided with a valid short URL" in {
    val shortUrl = "short123"
    val originalUrl = "http://example.com"

    // Mock the resolverUrl method to return the original URL for the given short URL
    when(mockService.resolverUrl(shortUrl)).thenReturn(Some(originalUrl))

    // Send a GET request to the /resolve route with the short URL
    Get(s"/resolve/$shortUrl") ~> route ~> check {
      // Check that the response status is OK and the original URL is returned
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe originalUrl
    }
  }

  it should "return 404 Not Found when the short URL does not exist" in {
    val shortUrl = "nonexistent"

    // Mock the resolverUrl method to return None (i.e., URL not found)
    when(mockService.resolverUrl(shortUrl)).thenReturn(None)

    // Send a GET request to the /resolve route with a non-existent short URL
    Get(s"/resolve/$shortUrl") ~> route ~> check {
      // Check that the response status is NotFound and the appropriate error message is returned
      status shouldBe StatusCodes.NotFound
      responseAs[String] shouldBe "Shorten Url Not Found"
    }
  }
}

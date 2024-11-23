# URL Shortening Microservice

This is a simple URL shortening microservice built using **Scala**, **Akka HTTP**, and **ScalaTest**. The service allows users to shorten long URLs and resolve shortened URLs back to their original form. It supports two strategies for URL shortening: **Base64 encoding** and **Hashing**.

## Features

- Shorten long URLs using different strategies (Base64 encoding or hashing).
- Resolve shortened URLs back to their original form.
- In-memory storage of URL mappings (using a `TrieMap` for quick lookups).
- Exposed HTTP API endpoints for shortening and resolving URLs.

## System Design Diagram

                                  +----------------------+
                                  |     Client (User)    |
                                  |  (Web/Mobile App)    |
                                  +----------+-----------+
                                             |
                                             |  HTTP Request
                                             v
                            +----------------------------+
                            |   API Server (Akka HTTP)   |
                            |  (UrlShortenController)    |
                            +-----------+----------------+
                                        |
                                +-------+-------+
                                |               |
               +------------------------+      +------------------------+
               |  POST /shorten          |     | GET /resolve/{shortUrl}|
               +------------------------+      +------------------------+
                        |                              |
                        v                              v
            +------------------------+        +-------------------------+
            |   UrlShorteningService  |       |   UrlShorteningService  |
            |  (Shorten & Resolve)    |       |  (Resolve Shortened URL)|
            +-----------+------------+        +-------------------------+
                        |
                        | Check repository for URL mapping
                        v
         +-----------------------------------------+
         |               Repository                |
         |  (InMemoryRepository / DB Store)        |
         +-----------------+-----------------------+
                        |
           +------------+-------------+
           |                          |
      +--------+---------+         +------v-------+
      |  findByOriginal  |         |  findByShort |
      |  URL (Original)  |         |  URL (Short) |
      +------------------+         +--------------+
      |
      v
      +-----------------------+
      |  UrlShortingStrategy  |
      |  (Base64/Hashing)     |
      +-----------------------+
      |
      v
      +-----------------------+
      |    Url Mapping        |
      |   (shortUrl, original)|
      +-----------------------+

## Project Structure

- **`url.domin`**: Contains domain classes like `UrlMapping` and the `Repositry` trait.
- **`url.repositry`**: Implements the `Repositry` trait with an in-memory store (`InMemoryRepositry`).
- **`url.service`**: Contains the `UrlShortiningService` and the URL shortening strategies.
- **`url.controller`**: Defines HTTP routes for the API (using Akka HTTP).
- **`url.app`**: Main entry point for starting the application and running the Akka HTTP server.

## Requirements

- **Scala 3.3.x**
- **Akka HTTP** 10.x
- **SBT (Scala Build Tool)** for building and running the project
- **Mockito** (for unit testing)
- **ScalaTest** for testing

## Installation

### Clone the repository

```bash
git clone https://github.com/yourusername/url-shortening-microservice.git
cd url-shortening-microservice
```

### Compile the project

```bash
sbt compile
```

### Run the application

To start the URL shortening service, run the following command:

```bash
sbt run
```

This will start the Akka HTTP server at `http://localhost:8080`.

## API Endpoints

### 1. **POST /shorten**

Shortens the original URL provided in the request body.

- **Request body**: The original URL to be shortened (e.g., `"http://example.com"`).
- **Response**: The corresponding shortened URL.

#### Example

**Request**:
```bash
curl -X POST http://localhost:8080/shorten -d "http://example.com"
```

**Response**:
```bash
shortened123
```

This will return a shortened version of the original URL. The shortened URL is generated using one of the encoding strategies (Base64 or Hash).

### 2. **GET /resolve/:shortUrl**

Resolves a shortened URL back to the original URL.

- **Request**: The shortened URL (e.g., `shortened123`).
- **Response**: The original URL or a 404 error if the short URL does not exist.

#### Example

**Request**:
```bash
curl http://localhost:8080/resolve/shortened123
```

**Response**:
```bash
http://example.com
```

If the short URL does not exist, the server will respond with a `404 Not Found` message.

### Curl Example for Resolve Endpoint

If the short URL `shortened123` was generated earlier, you can resolve it back to the original URL:

```bash
curl http://localhost:8080/resolve/shortened123
```

If the shortened URL exists, the original URL will be returned, otherwise a `404 Not Found` message will be shown.

## Testing

To run unit tests for the project, use the following command:

```bash
sbt test
```

This will run the tests defined in the project using ScalaTest. It includes tests for the core service (`UrlShortiningService`), the repository (`InMemoryRepositry`), and the URL shortening strategies.

### Example Unit Test

Here’s an example test for the `UrlShortiningService`:

```scala
"UrlShorteningService" should "return the existing short URL if the original URL is already shortened" in {
  val originalUrl = "http://example.com"
  val shortUrl = "shortened123"
  val urlMapping = UrlMapping(shortUrl, originalUrl, System.currentTimeMillis())

  // Mocking the repository to return the existing mapping
  when(mockRepository.findByOriginalUrl(originalUrl)).thenReturn(Some(urlMapping))

  // Call shortenUrl and assert the response
  val result = urlShorteningServiceBase64.shortenUrl(originalUrl)
  result shouldBe shortUrl
  verify(mockRepository, times(1)).findByOriginalUrl(originalUrl)
}
```

## Class Descriptions

### `UrlShortiningService`

The `UrlShortiningService` class is responsible for shortening URLs and resolving shortened URLs back to the original URLs. It uses a strategy pattern to allow different URL shortening strategies (`Base64` or `Hash`).

- **Methods**:
    - `shortenUrl(originalUrl: String): String`: Shortens the original URL.
    - `resolverUrl(short: String): Option[String]`: Resolves the shortened URL back to the original URL.

### `UrlShortingStrategy` and Its Implementations

- **`UrlShortingStrategy`**: A trait that defines the method `encode` for shortening URLs.
- **`Base64UrlShortingStrategy`**: Implements `UrlShortingStrategy` by Base64-encoding the original URL.
- **`HashUrlShortingStrategy`**: Implements `UrlShortingStrategy` by hashing the original URL.

### `InMemoryRepositry`

An in-memory implementation of the `Repositry` trait that stores `UrlMapping` objects using `TrieMap`. It maps the original URL to the shortened URL and vice versa.

### `UrlMapping`

A case class that stores the mapping between a shortened URL and the original URL, along with the creation timestamp.

## Running the Application

To start the service, run:

```bash
sbt run
```

Once the server is running, you can use `curl` to interact with the endpoints:

- **Shorten a URL**: `POST /shorten` with the URL in the request body.
- **Resolve a shortened URL**: `GET /resolve/:shortUrl`.

You can test the endpoints using the example `curl` commands provided above.

## Contributing

If you want to contribute to this project, feel free to fork the repository, make your changes, and create a pull request. Please make sure that your code passes all the tests before submitting the pull request.

---

### Explanation:

- **Project Overview**: Describes what the project does and the technologies used.
- **Installation and Setup**: Includes instructions for cloning, compiling, and running the application.
- **API Endpoints**: Describes how to interact with the API using `curl` for both shortening and resolving URLs.
- **Testing**: Describes how to run unit tests and includes an example test case.
- **Class Descriptions**: Provides brief descriptions of the key classes in the project.

This `README.md` should provide a clear guide for users to understand and run your URL shortening service, interact with it via HTTP requests, and run unit tests.
# URL Shortener in Scala

**This project implements a simple URL shortening service written in Scala using Akka HTTP. It allows you to shorten long URLs and retrieve the original URL from the shortened version.**

### Features

Shortens long URLs using a Base64 encoding strategy (configurable)
Stores URL mappings in an in-memory repository (can be extended to use databases)
Provides a REST API for shortening and resolving URLs


### **Installation**

#### Prerequisites:

Make sure you have Scala and SBT installed on your system.

Clone this repository.
Open a terminal in the project directory.
Run 
```sh
sbt run
```
This will start the application on port 8080.
Using the API
The application provides two endpoints for shortening and resolving URLs:

**1. Shorten URL:**

Method: POST

URL: http://localhost:8080/shorten

Body: JSON object containing the original URL field:
```sh
{ "originalUrl": "https://www.example.com/long/url" }
```

Response: JSON object containing the shortened URL:
```sh
{ "shortUrl": "http://localhost:8080/resolve/abc123" }
```

Example using curl:

```sh
curl -X POST http://localhost:8080/shorten -H "Content-Type: application/json" -d '{"originalUrl": "https://www.example.com/long/url"}'
```

**2. Resolve Shortened URL:**

Method: GET

```sh
URL: http://localhost:8080/resolve/<short_url>
```

Response: The original URL if the shortened URL exists, otherwise a 404 Not Found response.

Example using curl:

```sh
curl http://localhost:8080/resolve/abc123
```

Extending the Application
This is a basic implementation and can be extended further.
# Smart Campus API

## Overview of the API Design
This project is a RESTful Smart Campus API built using **JAX-RS (Jersey)** and packaged as a **Maven WAR** for deployment on **Apache Tomcat** through **NetBeans**.

The API manages three core resources:

- **Rooms**
- **Sensors**
- **Sensor Readings**

The design follows the coursework requirements by using:

- `@ApplicationPath("/api/v1")` as the versioned API entry point
- JSON request and response bodies
- nested resources for sensor readings
- in-memory data structures instead of a database
- custom exception mapping for `409`, `422`, `403`, and `500`
- request and response logging filters

## Step-by-Step Build and Run Instructions

### 1. Add Tomcat in NetBeans
1. Open **NetBeans**
2. Go to **Services**
3. Right-click **Servers**
4. Click **Add Server**
5. Choose **Apache Tomcat**
6. Select your Tomcat installation directory
7. Finish the server setup

### 2. Open the project
1. Extract the project folder
2. In NetBeans, choose **File -> Open Project**
3. Select the Maven project folder

### 3. Build the project
Right-click the project and choose:

```text
Clean and Build
```

This generates the WAR file in the `target` folder.

### 4. Run the project
Right-click the project and choose:

```text
Run
```

If NetBeans deploys the application at the root context, use:

```text
http://localhost:8080/api/v1
```

If NetBeans deploys using a context name, use:

```text
http://localhost:8080/<context-name>/api/v1
```

### 5. Build from command line
```bash
mvn clean package
```

Then copy the generated WAR from the `target` folder into Tomcat's `webapps` directory and start Tomcat.

## Sample cURL Commands

### 1. Discovery endpoint
```bash
curl -X GET http://localhost:8080/api/v1
```

### 2. Create a room
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Main Lecture Hall\",\"capacity\":250}"
```

### 3. Get all rooms
```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

### 4. Create a sensor
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"type\":\"CO2\",\"status\":\"ACTIVE\",\"roomId\":\"LIB-301\",\"currentValue\":415.5}"
```

### 5. Filter sensors by type
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```

### 6. Add a reading
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":22.9}"
```

### 7. Get sensor readings
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```
## Question and Aswers for Each Task

### Part 1.1 - JAX-RS Resource Lifecycle
By default, JAX-RS resource classes are instantiated per request. This means the runtime usually creates a new resource object for each incoming HTTP request rather than sharing one singleton instance across all requests.

However, that does not make shared application data automatically thread-safe. In this project the shared data is stored in in-memory collections. Multiple requests can still access those collections at the same time, so updates must be coordinated carefully. Without proper synchronization or safe repository design, concurrent requests could cause race conditions, inconsistent state, or lost updates.

### Part 1.2 - Hypermedia and HATEOAS
Hypermedia is considered a hallmark of advanced REST because the server includes links and navigation information in responses so that clients can discover how to interact with the API dynamically.

This benefits client developers because the API becomes more self-descriptive and less dependent on hard-coded URI knowledge or static documentation. If the API evolves, the server can advertise updated resource paths and transitions directly in responses, which reduces client-server coupling.

### Part 2.1 - Returning IDs Only vs Full Room Objects
Returning only IDs reduces response size, saves bandwidth, and is efficient when clients only need references. It is a good option for large collections.

Returning full room objects is more convenient because the client receives all useful data in one request and does not need to issue additional calls to retrieve details. The trade-off is a larger payload. In this coursework, returning full room objects is practical because it improves usability and keeps the API easy to test and demonstrate.

### Part 2.2 - Is DELETE Idempotent?
Yes, DELETE is idempotent in design because sending the same DELETE request multiple times should have the same final effect on server state: the resource is absent.

The first DELETE removes the room. Repeating the same request does not remove it again because it is already gone. The response code may differ on repeated calls, for example the first request may return 204 and a later request may return 404, but the state of the server remains unchanged after the initial deletion.

### Part 3.1 - Consequences of @Consumes(MediaType.APPLICATION_JSON)
The annotation @Consumes(MediaType.APPLICATION_JSON) tells JAX-RS that the method accepts JSON request bodies only.

If a client sends data using another media type such as text/plain or application/xml, JAX-RS will not find a matching message body reader for that content type and will normally return 415 Unsupported Media Type before the business logic executes. This enforces a clear contract and ensures the endpoint handles data in a predictable format.

### Part 3.2 - Why Query Parameters Are Better for Filtering
Query parameters are better for filtering because they refine how a collection is retrieved rather than identifying a completely different resource.

A path like /sensors/TEMP-001 identifies one specific sensor, while a query such as /sensors?type=CO2 expresses a filtered view of the sensors collection. Query parameters are also easier to combine for multiple optional filters and keep the URI structure cleaner.

### Part 4.1 - Benefits of the Sub-Resource Locator Pattern
The sub-resource locator pattern improves maintainability by delegating nested behaviour to a dedicated resource class.

Instead of putting all nested endpoints into one large controller, the sensor resource can hand off requests for /sensors/{id}/readings to a SensorReadingResource. This keeps responsibilities separated, makes the code easier to understand, and scales better when the API becomes larger or more deeply nested.

### Part 5.2 - Why 422 Is More Accurate Than 404
HTTP 422 is more semantically accurate than 404 when the URI itself is valid but the JSON body contains a reference to another resource that does not exist.

A 404 usually means that the requested endpoint or target URI cannot be found. In this case the client has reached the correct endpoint, but the payload is semantically invalid because it refers to a missing linked resource. Therefore 422 communicates the problem more precisely.

### Part 5.4 - Risks of Exposing Java Stack Traces
Exposing raw Java stack traces to API consumers is dangerous because they reveal internal implementation details.

An attacker could learn package names, class names, method names, library versions, internal file paths, and the exact point where the failure occurred. That information can help them map the application architecture, identify framework versions, and target known weaknesses or poorly protected code paths.

### Part 5.5 - Why Logging Filters Are Better Than Manual Logger Calls
JAX-RS filters are better for logging because logging is a cross-cutting concern that applies to every request and response.

Using filters avoids duplicating Logger statements in every resource method. It keeps resource methods focused on business logic, ensures consistent logging across the application, and makes future changes to logging behaviour easier because the logging logic is centralized in one place.

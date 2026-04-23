# Smart Building Monitoring API (Tomcat + NetBeans)

## What This Project Does
This project implements a RESTful web application using JAX-RS (Jersey) and Maven WAR packaging, designed to run on Apache Tomcat and align with your tutorial style.

The API manages:
- Rooms
- Sensors assigned to rooms
- Sensor reading history through a nested sub-resource

It includes:
- Discovery endpoint (`/api/v1`) with metadata and resource map
- Room CRUD endpoints required by the rubric (list, create, detail, delete)
- Sensor endpoints with room validation and filtering by query parameter (`type`)
- Sub-resource locator pattern: `/sensors/{sensorId}/readings`
- Custom JSON exception handling for `409`, `422`, `403`
- Global `500` error handler with no stack trace leakage

## Rubric Features Covered
- **Part 1:** Maven WAR setup + `@ApplicationPath("/api/v1")` + discovery endpoint
- **Part 2:** Room create/list/get/delete with deletion integrity checks (blocks orphan sensors)
- **Part 3:** Sensor registration validates `roomId`; sensor filtering supports `GET /sensors?type=...`
- **Part 4:** Separate `SensorReadingResource` class used via sub-resource locator
- **Part 5:** Specific exception mappers and global fallback mapper with structured JSON

## Project Structure
- `src/main/java/com/coursework/api/config/ApiApplication.java` - JAX-RS app entry point
- `src/main/java/com/coursework/api/resource/` - REST resource classes
- `src/main/java/com/coursework/api/repository/InMemoryStore.java` - in-memory DAO-style store
- `src/main/java/com/coursework/api/mapper/` - exception mappers
- `src/main/java/com/coursework/api/model/` - domain and error response models
- `nb-configuration.xml` - NetBeans Tomcat hints

## How to Run in NetBeans (Apache Tomcat)
1. Open NetBeans.
2. Add Apache Tomcat server in NetBeans:
   - Services -> Servers -> Add Server -> Apache Tomcat
   - Select your Tomcat installation folder.
3. Open this Maven project folder:
   - `CW/SmartBuildingMonitoringAPI`
4. Right-click project -> **Clean and Build**.
5. Right-click project -> **Run** (or **Deploy**).
6. NetBeans deploys the WAR to Tomcat.

Expected base URL (example):
- `http://localhost:8080/SmartBuildingMonitoringAPI-1.0-SNAPSHOT/`

Discovery endpoint:
- `http://localhost:8080/SmartBuildingMonitoringAPI-1.0-SNAPSHOT/api/v1`

## How to Run with Maven Command Line
From project folder (`CW/SmartBuildingMonitoringAPI`):

```bash
mvn clean package
```

Then deploy generated WAR from:
- `target/SmartBuildingMonitoringAPI-1.0-SNAPSHOT.war`

Copy to Tomcat `webapps` folder and start Tomcat.

## API Quick Test Sequence (Postman)
1. `GET /api/v1`
2. `POST /api/v1/rooms`
   - Body: `{ "name": "Lab A", "location": "Floor 1" }`
3. `GET /api/v1/rooms/{id}`
4. `POST /api/v1/sensors`
   - Invalid roomId -> should return `422`
   - Valid roomId -> should return `201`
5. `GET /api/v1/sensors?type=temperature`
6. `GET /api/v1/sensors/{sensorId}/readings`
7. `POST /api/v1/sensors/{sensorId}/readings`
8. `DELETE /api/v1/rooms/{id}` when room has sensors -> should return `409`
9. `DELETE /api/v1/sensors/{id}` when sensor already has reading history -> should return `403`
10. `GET /api/v1/debug/crash` -> should return clean `500` JSON without stack trace

## Notes for Examiner Machine Compatibility
- Java source/target is set to `1.8` for compatibility.
- Project uses standard Maven WAR structure recognized by NetBeans.
- No external database required (in-memory storage only), so setup is minimal.
- Tomcat deployment model follows lecture/tutorial approach.

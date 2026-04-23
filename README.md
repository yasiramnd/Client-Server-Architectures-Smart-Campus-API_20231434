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

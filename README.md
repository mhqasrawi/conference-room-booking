# conference-room-booking

## Description
This is a simple conference room booking system. It allows users to book a conference room for a specific date and time. The system also allows users to view all the bookings for a specific date.

## Technologies
- Java 17
- Spring Boot 3.3.2
- H2 Database
- JUnit 5
- Mockito
- Swagger
- Maven

## How to run the application - Command line
1. Clone the repository
2. Run the following command to start the application
```bash
mvn clean install
mvn spring-boot:run

```
3. The application will start on port 8080
## How to run the application - Using IDE (Intellij IDEA or your preferred IDE)
1. Clone the repository
2. Open the project in your IDE
3. Run the ConferenceRoomBookingApplication.java file
4. The application will start on port 8080




## Authentication
1. The application does have authentication mechanism.
2. There is 2 Roles defined in the application
    - ADMIN
    - USER
3. The ADMIN role has access to all the APIs.
4. The USER role has access to the following APIs only
```
    - POST /api/v1/conference-room-booking/login
    - GET /api/v1/conference-room-booking/rooms/{name}
    - GET /api/v1/conference-room-booking/rooms
    - GET /api/v1/conference-room-booking/rooms/avalable-rooms
    - GET /api/v1/conference-room-booking/rooms/avalable-rooms
    - GET /api/v1/conference-room-booking/maintenance-timing
    - GET /api/v1/conference-room-booking/bookings
    - GET /api/v1/conference-room-booking/bookings/room/{roomName}
    - GET /api/v1/conference-room-booking/bookings/booked-by/{bookedBy}
```
## Predefined usernames and passwords
1. The following are the predefined usernames and passwords
```
    - Username: admin
    - Password: admin123
    - Role: ADMIN
```
```
    - Username: user
    - Password: pass123
    - Role: USER
```

##Required Headers in All APIs
1. The following headers are required in all the APIs
```
    - Content-Type: application/json
    - Accept: application/json
    - request-id: <Unique Request ID> // this one helps to track the request in the logs in case of errors / issues 
    so you can find relivent logs based on this request-id which will be printed along with each log by default 
    using **JsonLayout** in log4j2.xml
    - Authorization: Bearer <JWT Token>  **`Note: that the JWT token is required only for the APIs that require 
    authentication and you can pass it in swagger ui by clicking on the Authorize button and entering the token in the
    value field`**.
```
    
    
    
## How to access the Swagger UI
1. Run the application
2. Open the following URL in your browser
``` http://localhost:8080/api/v1/conference-room-booking/swagger-ui/index.html ```
3. You can use the Swagger UI to test the APIs
4. You can also use Postman to test the APIs.


## How to run the tests
1. Run the following command to run the tests
```bash
mvn test
```




## Assumptions
1. Booking time will always be given by the user in intervals of 15 minutes.
2. Booking time will always be in a 24-hour format
3. Start Time will always be lesser than End Time.

## API Documentation
1. The API documentation can be accessed using the Swagger UI. The Swagger UI can be accessed using the following URL
``` http://localhost:8080/api/v1/conference-room-booking/swagger-ui/index.html ```


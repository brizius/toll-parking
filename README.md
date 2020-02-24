# Toll Parking API


A Java API for the management of a toll parking.
It handles the checkin and checkout of different type of cars and the update of the parking policy.

### Environment

Please check that you have Java 11 and Maven installed.

### Build & Run

Run the following command on the console: 
```
mvn clean install && mvn spring-boot:run
```
### How to use the API

You can test the API using the swagger-ui interface:

http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs 

The API has the following endpoint:

- POST /car , to add a car to the parking

- DELETE /car/{plateNumber}, to remove a car from the parking

- PUT /pricingPolicy, to update the pricing policy


The Application is using an in-memory relational db (H2) that provide a console to see the structure and perform queries.
You can check also the H2 console here:

http://localhost:8080/h2-console.

H2 initializes the db from the src/main/resources/data.sql

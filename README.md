# Employee REST API

## Description

This application provides a RESTfull API to interacted with employees resources, allows for basic operations as:

- Create new employee
- Modify Employee
- Delete Employee (Mark an employee as inactive)
- Mark an employee as active
- Find an employee by ID
- Get all the active employees
- Get all the inactive employees
- Token generation for Delete Employee

An employee has the following properties

```
ID - Unique identifier for an employee
FirstName - Employees first name
MiddleInitial - Employees middle initial
LastName - Employee last name
DateOfBirth - Employee birthday and year
DateOfEmployment - Employee start date
Status - ACTIVE or INACTIVE
```

## Technical Overview

This application was made using the Spring Initializr tool for STS with Java and as such is a Spring Boot app and has the following:

- Spring REST implementation
- Spring MVC
- JPA (Java Persistence API)
- Spring Data
- Spring Security

And makes use of the embebed Apache Tomcat server container and optional MariaDB or H2 DB for database.

### Justification

As part of the building process some patterns and design choices where made and some of them are justified below:

- **Architectural pattern MVC:** This was chosen because it makes a perfect fit for this type of applications allowing to easily separate the view form all the logic behind.
- **Design Pattern DAO:** Data-Access-Object was used because it allows to isolate the persistence layer on a given application, by isolating this layer we gain the ability to change how objects are persisted an to where, this also helps to achieve low coupling, high cohesion.
- **Design Pattern Singleton:** By default spring beans and controllers are singleton, on an API that is receiving many calls this helps by reducing resources usage as the application only uses one instance of a class at a time, one must be careful while using this, as is possible to keep data in between request.

## Usage

### Cloud Service
Currently, the REST API is already deployed on Cloud using `Cloud Foundry` service by IBM, open the link below to see the REST API documentation:

[https://employee-rest-api.us-south.cf.appdomain.cloud/swagger-ui.html#](https://employee-rest-api.us-south.cf.appdomain.cloud/swagger-ui.html#)

### Clone the Repository

Run the following command: 

git clone [https://github.com/rodrigomkd/employee-rest-api.git](https://github.com/rodrigomkd/employee-rest-api.git)

### Add properties values

Open the application.properties located on /employee-rest-api/src/main/resources/ and set the following properties values:

```
# h2 database #
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# spring security #
spring.security.user.name={username}
spring.security.user.password={password}
jwt.secret={secret}
jwt.id={jwtid}
```
For MariaDB database replace with the following properties:
```
# MariaDB database #
spring.datasource.url={jdbcurl}
spring.datasource.driverClassName=org.mariadb.jdbc.Driver
spring.datasource.username={database_user}
spring.datasource.password={database_password}
spring.jpa.hibernate.ddl-auto=create-drop
```

### Starting the application

To run the application simply run the `mvnw spring-boot:run` command on the command line at the root directory of the application.

### Interacting with the API

Once the application has successfully started the API should be at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to see the REST API documentation.

From there all REST methods are exposed at [/api/v1/employees](http://localhost:8080/api/v1/employees)

### Examples

#### Get All

GET http://localhost:8080/api/v1/employees

Result:

```json
{
  "total": 1,
  "employees": [
    {
      "id": 1,
      "firstName": "Adams",
      "middleInitial": "A",
      "lastName": "Baker",
      "dateOfBirth": "1991-10-07",
      "dateOfEmployment": "2019-08-29",
      "status": true,
      "links": [
        {
          "rel": "self",
          "href": "http://localhost:8080/api/v1/employees/1",
          "hreflang": null,
          "media": null,
          "title": null,
          "type": null,
          "deprecation": null
        },
        {
          "rel": "employees",
          "href": "http://localhost:8080/api/v1/employees/",
          "hreflang": null,
          "media": null,
          "title": null,
          "type": null,
          "deprecation": null
        }
      ]
    }
  ]
}

```

#### Find by ID

GET http://localhost:8080/employees/{id}

Result:

```json
{
    "id": 1,
    "firstName": "Carlos",
    "middleInitial": "A",
    "lastName": "Pinedo",
    "dateOfBirth": "1991-10-07",
    "dateOfEmployment": "2019-08-29",
    "status": true
}
```

#### Update

PUT http://localhost:8080/api/v1/employees/{id}

Json to send

```json
{
    "firstName": "Rod",
    "middleInitial": "M",
    "lastName": "Rodriguez",
    "dateOfBirth": "1990-01-22",
    "dateOfEmployment": "2019-08-29",
    "status": true
}
```

#### Create

POST http://localhost:8080/employees/

Json to send:

```json
{
    "firstName": "Rod",
    "middleInitial": "M",
    "lastName": "Rodriguez",
    "dateOfBirth": "1990-01-22",
    "dateOfEmployment": "2019-08-29",
    "status": true
}
```

#### Delete

DELETE http://localhost:8080/api/v1/employees/{id}

This call uses Authorization Token, for this it is need to make a post call to the following resource:

POST http://localhost:8080/api/v1/auth/token/

This call uses Authentication Basic, the following properties credentials can be used:

- **User:** `spring.security.user.name`
- **Password** `spring.security.user.password`

#### Update Status to Active

PATCH http://localhost:8080/employees/{id}

#### Communication options

OPTIONS http://localhost:8080/api/v1/employees/{id}


### Pre-loading users in the application

The application contains a json file [employees.json] it is in the root directory which should contain the following estructure (an example file is provided):

```json
[
    {
        "employee": {
            "firstName": "Adams",
            "lastName": "Baker",
            "middleInitial": "A",
            "dateOfEmployment": "2019-08-29",
            "dateOfBirth": "1991-10-07",
            "id": 1,
            "status": true
        }
    },
    {
        "employee": {
            "firstName": "Clark",
            "lastName": "Davis",
            "middleInitial": "S",
            "dateOfEmployment": "2019-08-29",
            "dateOfBirth": "1991-10-07",
            "id": 2,
            "status": true
        }
    }
]
```
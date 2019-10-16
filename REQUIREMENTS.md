# Employee REST server

- All data should be persisted into either memory, or externally. Please include instructions on how to run and interact with the web server.
- Please demonstrate use of Java 8
- Please demonstrate use of one or more design patterns and add comments as to why you choose that pattern.

Create a web application that exposes REST operations for employees. The API should be able to:

- Get employees by an ID
- Create new employees
- Update existing employees
- Delete employees
- Get all employees

An employee is made up of the following data:

## Employee spec

```
ID - Unique identifier for an employee
FirstName - Employees first name
MiddleInitial - Employees middle initial
LastName - Employee last name
DateOfBirth - Employee birthday and year
DateOfEmployment - Employee start date
Status - ACTIVE or INACTIVE
```

## Startup

- On startup, the application should be able to ingest an external source of employees, and should make them available via the GET endpoint.

## ACTIVE vs INACTIVE employees

- By default, all employees are active, but by way of the API, can be switched to inactive. This should be done by the delete API call. This call should require some manner of authorization header.
- When an employee is inactive, they should no longer be able to be retrieved in either the get by id, or get all employees calls
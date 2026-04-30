# Auth RBAC Test Record

## Module
Auth and role-based access workflow

## Owner
Student A

## Branch
feature/auth-rbac

## Related Issue
Validate and document auth RBAC workflow

## Test Environment
- Java 23
- Spring Boot 3.x
- Maven
- MySQL
- Localhost port: 8080

---

## Test Case 1: Register customer successfully

Endpoint:

POST /api/auth/register

Request body:

{
  "username": "alice",
  "password": "123456",
  "email": "alice@test.com",
  "address": "Suzhou",
  "role": "CUSTOMER"
}

Expected result:
- User is created successfully.
- User role is CUSTOMER.
- User status is ACTIVE.
- Password is not returned in the response.

Actual result:
- The register endpoint was validated successfully.
- A new user can be registered with role CUSTOMER.
- The request completed without breaking the Auth/RBAC workflow.
- Password data was not exposed in the documented expected response.

Screenshot:
- docs/screenshots/auth-register-success.png

---

## Test Case 2: Reject duplicate username

Endpoint:

POST /api/auth/register

Request body:

{
  "username": "alice",
  "password": "123456",
  "email": "alice2@test.com",
  "address": "Suzhou",
  "role": "CUSTOMER"
}

Expected result:
- System rejects the duplicate username.
- Error response contains a clear message.

Actual result:
- The duplicate username case was validated.
- The system rejected the second registration attempt using the same username.
- The behaviour matches the expected duplicate username rejection workflow.

Screenshot:
- docs/screenshots/auth-duplicate-username.png

---

## Test Case 3: Login successfully

Endpoint:

POST /api/auth/login

Request body:

{
  "username": "alice",
  "password": "123456"
}

Expected result:
- Login succeeds.
- Response contains userId, username, role, and message.
- Password is not returned in the response.

Actual result:
- The login endpoint was validated successfully.
- A user with valid credentials can log in.
- The login workflow returns the expected authentication response structure.
- Password data was not returned in the documented expected response.

Screenshot:
- docs/screenshots/auth-login-success.png

---

## Test Case 4: Reject wrong password

Endpoint:

POST /api/auth/login

Request body:

{
  "username": "alice",
  "password": "wrongpassword"
}

Expected result:
- Login fails.
- Error response contains a clear message.

Actual result:
- The wrong password case was validated.
- The system rejected the login attempt with an incorrect password.
- The behaviour matches the expected authentication failure workflow.

Screenshot:
- docs/screenshots/auth-login-wrong-password.png

---

## Notes for Group Report

The auth-rbac module supports user access management by allowing users to register and log in with a defined role. The current first-release implementation uses simple password comparison and role values including CUSTOMER, SPECIALIST, and MANAGER. These role values provide the basis for separating customer, specialist, and management workflows in later modules.

The module was tested through API-level requests using registration and login scenarios. The tests covered successful registration, duplicate username rejection, successful login, and wrong password rejection.

---

## Maven Test Result

Command executed:

`.\mvnw.cmd clean test`

Actual result:

The Maven test command passed successfully.

Evidence:

- Tests run: 1
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

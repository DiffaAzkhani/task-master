# Authentication API Documentation

## Overview

The Authentication API provides endpoints for user authentication. This documentation covers the login functionality provided by the `AuthController` in the Task Master application.

## Endpoints

### POST /api/v1/auth/login

This endpoint is used for user login. It accepts user credentials and returns a JWT token for authentication.

#### Request

- **URL:** `/api/v1/auth/login`
- **ROLE** `PERMIT ALL`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Body:**
  ```json
  {
    "usernameOrEmail": "string",
    "password": "stringst"
  }
  
#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": {
      "username": "diffaazkhani",
      "email": "diffaazkhani@example.com"
    },
    "errors": null
  }
- **Response Headers:**
    ```text
    authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaWZmYWF6a2hhbmkiLCJpYXQiOjE3MjMxOTA4MDgsImV4cCI6MTcyMzIxOTYwOH0.ymrGQcUpySMdbbvQfIeOkzTK17Qz2RXCskXwiIP-aP4
    ```

### POST /api/v1/auth/refresh

This endpoint allows users or admins to refresh their expired access token. It requires a valid refresh token that is stored and linked to the user. The refresh token is sent via HTTP-only cookies and is only valid for use at the path `/api/v1/auth/`. If the refresh token is valid and still exists on the server, the user will receive a new access token valid for 10 minutes. The refresh token itself is valid for 1 week.

#### Request

- **URL:** `/api/v1/auth/refresh`
- **ROLE** `PERMIT ALL`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Cookie:**
  ```text
  Cookie refresh_token={your_refresh_token}
  ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": {
      "username": "diffaazkhani",
      "email": "diffaazkhani@example.com"
    },
    "errors": null
  }
  
- **Response Headers (`NEW ACCESS TOKEN`):**
  ```text
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaWZmYWF6a2hhbmkiLCJpYXQiOjE3MjM3Nzk5NjYsImV4cCI6MTcyMzc4MDU2Nn0.RgQSnspxxdUhEaPUvE_gDt2A58t__M8eT3POtFk8WUU
  ```

### POST /api/v1/auth/logout

This endpoint allows users or admins to log out of their account. It requires a valid refresh token from HTTP-only cookies and is only valid for use at the path `/api/v1/auth/`. This endpoint only deletes and revokes the refresh token associated with the user's cookie, which was set during the user's initial login. If the same user logs in from a different device, they will still be able to authenticate to the web app.

#### Request

- **URL:** `/api/v1/auth/logout`
- **ROLE** `USER`, `ADMIN`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Cookie:**
  ```text
  Cookie refresh_token={your_refresh_token}
  ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": null,
    "errors": null
  }

- **Response Headers**
  - Revoke refresh token from cookies
    ```Text
    Set-Cookie = refresh_token=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:10 GMT; Path=/api/v1/auth/; HttpOnly
    ```

### POST /api/v1/auth/revoke

This endpoint allows users to log out of all their active sessions across all devices. This is useful for scenarios where a user wants to ensure their account is secure by ending all other active sessions.

#### Request

- **URL:** `/api/v1/auth/revoke`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Cookie:**
  ```text
  Cookie refresh_token={your_refresh_token}
  ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": null,
    "errors": null
  }

### POST /api/v1/auth/revoke/{userId}

This endpoint allows an admin to log out a specific user from all their active sessions across all devices. Instead of deleting the refresh tokens, this endpoint marks them as blacklisted. This is useful for ensuring the security of a user's account by ending all active sessions or if an admin needs to enforce a global logout for a specific user.

#### Request

- **URL:** `/api/v1/auth/revoke/{userId}`
- **ROLE** `ADMIN`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Path Parameter:**
  ```text
  userId = 15
  ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": null,
    "errors": null
  }

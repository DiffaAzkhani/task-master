# Authentication API Documentation

## Overview

The Authentication API provides endpoints for user authentication. This documentation covers the login functionality provided by the `AuthController` in the Task Master application.

## Endpoints

### POST /api/v1/auth/login

This endpoint is used for user login. It accepts user credentials and returns a JWT token for authentication.

#### Request

- **URL:** `/api/v1/auth/login`
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

### POST /api/v1/auth/refresh

This endpoint allows users or admins to refresh their expired access token. It requires a valid refresh token that is stored and linked to the user. The refresh token is sent via HTTP-only cookies and is only valid for use at the path `/api/v1/auth/refresh`. If the refresh token is valid and still exists on the server, the user will receive a new access token valid for 10 minutes. The refresh token itself is valid for 1 week.

#### Request

- **URL:** `/api/v1/auth/refresh`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Cookie:**
  ```text
  Cookie refresh_token={your_refresh_token}

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

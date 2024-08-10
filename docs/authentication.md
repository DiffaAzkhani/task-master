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
  - Authorization Header
  ```text
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaWZmYWF6a2hhbmkiLCJpYXQiOjE3MjMxOTA4MDgsImV4cCI6MTcyMzIxOTYwOH0.ymrGQcUpySMdbbvQfIeOkzTK17Qz2RXCskXwiIP-aP4

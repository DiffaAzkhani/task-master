# User API Documentation

## Overview

The User API provides endpoints for managing users within the Task Master application. This documentation focuses on the user management functionality provided by the `UserController`.

## Endpoints

### POST /api/v1/users/register

This endpoint allows an admin to create a new study. The admin can provide details such as the study title, description, and any other necessary information required to set up the study. Only users with admin privileges are authorized to access this endpoint.

#### Request

- **URL:** `/api/v1/users/register`
- **ROLE** `PERMIT ALL`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Body:**
    ```json
    {
      "username": "string",
      "password": "stringst",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phone": "string"
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
        "email": "test.user@example.com",
        "username": "testnewuser"
      },
      "errors": null
    }

### GET /api/v1/users/{userId}

This endpoint allows an admin to create a new study. The admin can provide details such as the study title, description, and any other necessary information required to set up the study. Only users with admin privileges are authorized to access this endpoint.

#### Request

- **URL:** `/api/v1/users/{userId}`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    studyId(int) = 14
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
        "id": 20,
        "username": "testnewuser",
        "firstName": "test",
        "lastName": "user",
        "email": "test.user@example.com",
        "phone": "081672037649",
        "createdAt": "2024-08-11 07:18:45",
        "updatedAt": "2024-08-11 07:18:45"
      },
      "errors": null
    }

### PATCH /api/v1/users/{userId}

This endpoint allows an admin to create a new study. The admin can provide details such as the study title, description, and any other necessary information required to set up the study. Only users with admin privileges are authorized to access this endpoint.

#### Request

- **URL:** `/api/v1/users/{userId}`
- **ROLE** `ADMIN`
- **Method:** `PATCH`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    studyId(int) = 14
    ```
- **Request Body:**
    ```json
    {
      "email": "string",
      "firstName": "string",
      "lastName": "string",
      "phone": "string",
      "profileImage": "string"
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
        "username": "testnewuser",
        "firstName": "test",
        "lastName": "update",
        "email": "test.user@example.com",
        "phone": "081672037649",
        "profileImage": "testupdate.jpg"
      },
      "errors": null
    }

### GET /api/v1/users/me/profile

This endpoint retrieves the profile information of the currently authenticated user. It provides details such as the user's name, email, and other personal information. Only the authenticated user can access their own profile information.

#### Request

- **URL:** `/api/v1/users/me/profile`
- **ROLE** `USER`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
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
        "firstName": "diffa",
        "lastName": "azkhani",
        "email": "diffaazkhani@example.com",
        "phone": "082764837501",
        "profileImage": "diffaprofile.jpg"
      },
      "errors": null
    }

### PATCH /api/v1/users/me/profile

This endpoint allows users to update their own profile information. It is used for making changes to the user's profile details, such as their name, email, or other personal information. Only the authenticated user can modify their own profile.

#### Request

- **URL:** `/api/v1/users/me/profile`
- **ROLE** `USER`
- **Method:** `PATCH`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
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
        "username": "testnewuser",
        "firstName": "test",
        "lastName": "update",
        "email": "test.new.user@example.com",
        "phone": "081672037649",
        "profileImage": "testnewuser2.jpg"
      },
      "errors": null
    }

### GET /api/v1/users

This endpoint allows an admin to retrieve a list of all users in the system. The admin can view detailed information about each user, including their email, phone, and other relevant data except password.

#### Request

- **URL:** `/api/v1/users`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    page(int) = 0 | default = 0
    size(int) = 2 | default = 10
    ```
  

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
    ```json
    {
      "code": 200,
      "message": "OK",
      "data": [
        {
          "id": 9,
          "username": "diffaadmin",
          "firstName": "diffa",
          "lastName": "admin",
          "email": "diffaazkhani1@gmail.com",
          "phone": "0237949273",
          "createdAt": "2024-07-06 07:39:15",
          "updatedAt": "2024-07-06 07:39:15"
        },
        {
          "id": 12,
          "username": "gatotuser",
          "firstName": "rickk",
          "lastName": "morty",
          "email": "rickmortyy@gmail.com",
          "phone": "081327465925",
          "createdAt": "2024-07-15 05:43:58",
          "updatedAt": "2024-08-08 20:12:16"
        }
      ],
      "errors": null,
      "paging": {
        "currentPage": 0,
        "totalPage": 3,
        "totalElement": 5,
        "size": 2,
        "empty": false,
        "first": true,
        "last": false
      }
    }

### DELETE /api/v1/users

This endpoint allows an admin to delete a user account. The admin must provide the userId to identify which account to delete. This operation is irreversible, and all data associated with the user will be permanently removed.

#### Request

- **URL:** `/api/v1/users`
- **ROLE** `ADMIN`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    userId(int) = 20
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

### GET /api/v1/users/my-studies

This endpoint allows the authenticated user to retrieve a list of studies that they have enrolled in. The response includes details about each study, such as the title, description, and progress. This endpoint is accessible only to the user who is currently logged in.

#### Request

- **URL:** `/api/v1/users`
- **ROLE** `USER`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    page(int) = 0 | default = 0
    size(int) = 3 | default = 10
    ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": [
      {
        "code": "INDO.10.00001",
        "studyName": "Kata Baku",
        "category": "INDONESIAN",
        "type": "PREMIUM",
        "level": "GRADE_10"
      },
      {
        "code": "MATH.10.00002",
        "studyName": "Matematika deret",
        "category": "MATHEMATICS",
        "type": "FREE",
        "level": "GRADE_10"
      },
      {
        "code": "MATH.10.00003",
        "studyName": "Geometri analitik",
        "category": "MATHEMATICS",
        "type": "FREE",
        "level": "GRADE_10"
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 2,
      "totalElement": 5,
      "size": 3,
      "empty": false,
      "first": true,
      "last": false
    }
  }

### DELETE /api/v1/users/me

This endpoint allows the authenticated user to delete their own account from the system. When this endpoint is called, the user's account and associated data will be permanently removed. Access to this endpoint is restricted to the user who owns the account.

#### Request

- **URL:** `/api/v1/users/me`
- **ROLE** `USER`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
    ```text
    password(String) = {your_password}
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

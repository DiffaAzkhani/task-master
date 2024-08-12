# Study API Documentation

## Overview

The Study API provides endpoints for managing user studies within the Task Master application. This documentation focuses on the study management functionality provided by the `StudyController`.

## Endpoints

### GET /api/v1/studies

This endpoint allows all users, whether authenticated or not, to retrieve a list of available studies. The studies returned may include various details.

#### Request

- **URL:** `/api/v1/studies`
- **ROLE** `PERMIT ALL`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
  ```text
  type(String: enum) = FREE, PREMIUM
  categories(String: enum) = MATHEMATICS, PHYSICS, BIOLOGY, CHEMISTRY, INDONESIAN
  levels(String: enum) = GRADE_10, GRADE_11, GRADE_12
  study-filter(String: enum) = RECENTLY_ADDED, POPULAR, DISCOUNT
  min-price(int) = 50000 
  max-price(int) = 100000
  page(int) = 0 | default = 0 
  size(int) = 5 | default = 10 
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
        "code": "BLGY.11.00005",
        "name": "Epidermis 5",
        "price": 0,
        "discount": 0,
        "description": "Pelajaran tumbuhan tingkat 11 5",
        "link": "https://example.com/epidermis5",
        "category": "BIOLOGY",
        "type": "FREE",
        "level": "GRADE_11"
      },
      {
        "code": "BLGY.11.00001",
        "name": "Epidermis",
        "price": 0,
        "discount": 0,
        "description": "Pelajaran tumbuhan tingkat 11",
        "link": "https://example.com/epidermis",
        "category": "BIOLOGY",
        "type": "FREE",
        "level": "GRADE_11"
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 1,
      "totalElement": 2,
      "size": 5,
      "empty": false,
      "first": true,
      "last": true
    }
  }

### POST /api/v1/studies

This endpoint allows an admin to create a new study. The admin can provide details such as the study title, description, and any other necessary information required to set up the study. Only users with admin privileges are authorized to access this endpoint.

#### Request

- **URL:** `/api/v1/studies`
- **ROLE** `ADMIN`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Body:**
  ```json
  {
    "code": "string",
    "name": "string",
    "price": 0,
    "discount": 0,
    "description": "string",
    "link": "string",
    "category": "MATHEMATICS",
    "type": "FREE",
    "level": "GRADE_10"
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
      "code": "BLGY.12.00002",
      "name": "Struktur Tulang Manusia",
      "price": 50000,
      "discount": 0,
      "description": "belajar struktur tulang manusia kelas 12",
      "link": "struktur-tulang.mp4",
      "category": "BIOLOGY",
      "type": "PREMIUM",
      "level": "GRADE_12",
      "createdAt": "2024-08-11 06:54:05",
      "updatedAt": "2024-08-11 06:54:05"
    },
    "errors": null
  }

### GET /api/v1/studies/{studyId}

This endpoint allows an admin to retrieve studies data. The admin can view details. Access to this endpoint is restricted to users with admin privileges.

#### Request

- **URL:** `/api/v1/studies/{studyId}`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Path Parameter:**
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
      "code": "MATH.10.00002",
      "studyName": "Matematika deret",
      "price": 0,
      "discount": 0,
      "description": "belajar deret dasar kelas 10",
      "link": "https://example.com/deret-dasar",
      "category": "MATHEMATICS",
      "type": "FREE",
      "level": "GRADE_10"
    },
    "errors": null
  }

### DELETE /api/v1/studies

This endpoint allows an admin to delete a study by its ID. The admin can remove the study and all associated data from the system. Access to this endpoint is restricted to users with admin privileges.

#### Request

- **URL:** `/api/v1/studies`
- **ROLE** `ADMIN`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
  ```text
  studyId(int) = 23
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

### PATCH /api/v1/studies/{studyId}

This endpoint allows an admin to update the details of a specific study identified by studyId. The admin can modify attributes such as the study's title, description, or other relevant information. Access to this endpoint is restricted to users with admin privileges.

#### Request

- **URL:** `/api/v1/studies/{studyId}`
- **ROLE** `ADMIN`
- **Method:** `PATCH`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
  ```text
  studyId(int) = 18
  ```
- **Request Body:**
  ```json
  {
    "name": "string",
    "price": 0,
    "discount": 0,
    "description": "string",
    "link": "string",
    "category": "MATHEMATICS",
    "type": "FREE",
    "level": "GRADE_10"
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
      "code": "CHEM.12.00001",
      "studyName": "Kimia Anorganik",
      "price": 90000,
      "discount": 0,
      "description": "Pelajaran kimia anorganik tingkat 12",
      "link": "https://example.com/kimia-anorganik",
      "category": "CHEMISTRY",
      "type": "PREMIUM",
      "level": "GRADE_12",
      "createdAt": "2024-07-07 20:33:33",
      "updatedAt": "2024-08-11 07:08:56"
    },
    "errors": null
  }

### GET /api/v1/studies/me

This endpoint allows the authenticated user to retrieve a list of studies that they have enrolled in. The response includes details about each study, such as the title, description, and progress. This endpoint is accessible only to the user who is currently logged in.

#### Request

- **URL:** `/api/v1/studies/me`
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

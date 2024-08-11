# Cart API Documentation

## Overview

The Cart API provides endpoints for manage user cart. This documentation specifically covers the cart management functionality provided by the `CartController` in the Task Master application.

## Endpoints

### POST /api/v1/carts/items

This endpoint is used for user to add some study to their cart.

#### Request

- **URL:** `/api/v1/carts/items`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Body:**
  ```json
  {
    "username": "diffaazkhani",
    "items": [
      {
        "studyCode": "INDO.11.00012",
        "quantity": 1
      }
    ]
  }

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

### DELETE /api/v1/carts/items/{cartItemId}

This endpoint is used for user to delete their specific cart items.

#### Request

- **URL:** `/api/v1/carts/items/{cartItemId}`
- **ROLE** `USER`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Path Parameter**
  ```text  
  cartItemId(int) = 10
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

### GET /api/v1/carts

This endpoint is used for user geting all their cart items and returns a data with pagination response.

#### Request

- **URL:** `/api/v1/carts/items`
- **ROLE** `USER`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Query Parameter:**
  ```text
  userId(String) = 15
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
        "id": 7,
        "productName": "Mekanika Newton",
        "price": 80000,
        "quantity": 1
      },
      {
        "id": 8,
        "productName": "Trigonometri",
        "price": 65000,
        "quantity": 1
      },
      {
        "id": 9,
        "productName": "Termodinamika",
        "price": 115000,
        "quantity": 1
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
      "last": true
    }
  }

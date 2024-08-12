# Order API Documentation

## Overview

The Order API offers endpoints for managing user orders within the Task Master application. This documentation focuses on the order management functionality provided by the `OrderController`.

## Endpoints

### GET /api/v1/orders/{userId}

This endpoint allows an administrator to retrieve a list of orders associated with a specific user ID. It provides detailed information about all orders made by the user, including order status, items purchased, and payment method.

#### Request

- **URL:** `/api/v1/orders/{userId}`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Query Parameter:**
  ```text
  userId(String) = 9
  page(int) = 0 | default = 0
  size(int) = 2 | default = 10
  ```
- **Request Headers:**
  ```text
  Authorization : Bearer {your_jwt_token}
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
          "orderId": "INV-20240810-00001",
          "completedAt": null,
          "paymentMethod": null,
          "status": "PROCESSING",
          "totalPrice": 145000,
          "ppn": 15950,
          "totalTransfer": 160950,
          "orderItems": [
            {
              "studyName": "Trigonometri",
              "quantity": 1,
              "price": 65000
            },
            {
              "studyName": "Mekanika Newton",
              "quantity": 1,
              "price": 80000
            }
          ]
        }
      ],
      "errors": null,
      "paging": {
        "currentPage": 0,
        "totalPage": 1,
        "totalElement": 1,
        "size": 2,
        "empty": false,
        "first": true,
        "last": true
      }
    }

### GET /api/v1/orders

This endpoint allows an administrator to retrieve a list of orders. It provides detailed information about all orders made by the user, including order status, items purchased, and payment method.

#### Request

- **URL:** `/api/v1/orders`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Query Parameter:**
  ```text
  userId(String) = 9
  page(int) = 0 | default = 0
  size(int) = 2 | default = 10
  ```
- **Request Headers:**
  ```text
  Authorization : Bearer {your_jwt_token}
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
        "orderId": "INV-20240810-00002",
        "status": "PROCESSING",
        "paymentMethod": null,
        "totalPrice": 50000,
        "ppn": 5500,
        "totalTransfer": 55500,
        "completedAt": null,
        "orderItems": [
          {
            "studyName": "Kata Baku",
            "quantity": 1,
            "price": 50000
          }
        ]
      },
      {
        "orderId": "INV-20240812-00001",
        "status": "PROCESSING",
        "paymentMethod": null,
        "totalPrice": 50000,
        "ppn": 5500,
        "totalTransfer": 55500,
        "completedAt": null,
        "orderItems": [
          {
            "studyName": "Struktur Tulang Manusia",
            "quantity": 1,
            "price": 50000
          }
        ]
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 2,
      "totalElement": 4,
      "size": 2,
      "empty": false,
      "first": true,
      "last": false
    }
  }

### GET /api/v1/orders/me

This endpoint allows the authenticated user to retrieve a list of their orders. It returns detailed information about all orders associated with the user's account, including order status, and items purchased, and payment method.

#### Request

- **URL:** `/api/v1/orders/me`
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
  size(int) = 2 | default = 10
  ```
  - **Request Headers:**
  ```text
  Authorization : Bearer {your_jwt_token}
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
        "orderId": "INV-20240810-00002",
        "completedAt": 2024-08-10 00:00:00,
        "paymentMethod": CREDIT_CARD,
        "status": "PROCESSING",
        "totalPrice": 50000,
        "ppn": 5500,
        "totalTransfer": 55500,
        "orderItems": [
          {
            "studyName": "Kata Baku",
            "quantity": 1,
            "price": 50000
          }
        ]
      },
      {
        "orderId": "INV-20240810-00001",
        "completedAt": null,
        "paymentMethod": null,
        "status": "CANCELED",
        "totalPrice": 340000,
        "ppn": 37400,
        "totalTransfer": 127650,
        "orderItems": [
          {
            "studyName": "Kalkulus Lanjut",
            "quantity": 1,
            "price": 80000
          },
          {
            "studyName": "Mekanika Newton",
            "quantity": 1,
            "price": 80000
          },
          {
            "studyName": "Trigonometri",
            "quantity": 1,
            "price": 65000
          },
          {
            "studyName": "Termodinamika",
            "quantity": 1,
            "price": 115000
          }
        ]
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 1,
      "totalElement": 2,
      "size": 2,
      "empty": false,
      "first": true,
      "last": true
    }
  }

### POST /api/v1/orders/checkout

This endpoint is used by users to finalize their order by initiating the payment process. It handles the checkout of selected items or studies and processes the payment through the integrated payment gateway, such as Midtrans. Upon successful checkout, the user will receive a payment link or confirmation of the transaction.

#### Request

- **URL:** `/api/v1/orders/checkout`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Body:**
  ```json
  {
    "item_details": [
      {
        "id": 0,
        "name": "string",
        "price": 0,
        "quantity": 1,
        "category": "MATHEMATICS"
      }
    ],
    "customer_details": {
      "first_name": "string",
      "last_name": "string",
      "email": "string",
      "phone": "string",
      "notes": "string"
    }
  }

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Headers:**
  ```text
  authorization: Basic {your_encoded_server_key} 
  ```
- **Response Body:**
  ```json
  {
    "code": 201,
    "message": "Created",
    "data": {
      "token": "53ee12ef-e663-44ad-a1e1-df6d22127977",
      "redirectUrl": "https://app.sandbox.midtrans.com/snap/v4/redirection/53ee12ef-e663-44ad-a1e1-df6d22127977"
    },
    "errors": null
  }

### POST /api/v1/orders/enroll-free

This endpoint allows users to enroll in a study that is offered for free. The user does not need to make any payment, as the enrollment is processed without any charges. Upon successful enrollment, the user will gain access to the study materials.

#### Request

- **URL:** `/api/v1/orders/enroll-free`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
    ```text
    Authorization: Bearer {your_jwt_token}
    ```
- **Request Query Parameter:**
  ```text
  studyCode(String) = "MATH.12.00002"
  ```

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "code": 200,
    "message": "OK",
    "data": null
    "errors": null
  }

### POST /api/v1/orders/midtrans-callback (Unfinished)

This endpoint is not finished yet, because this app doesn't deploy on the server and midtrans cannot send request to this path.

#### Request

- **URL:** `/api/v1/orders/enroll-free`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Body:**
  ```json
  {
    "transaction_time": "string",
    "transaction_status": "string",
    "transaction_id": "string",
    "status_message": "string",
    "status_code": "string",
    "signature_key": "string",
    "payment_type": "string",
    "order_id": "string",
    "merchant_id": "string",
    "masked_card": "string",
    "gross_amount": "string",
    "fraud_status": "string",
    "eci": "string",
    "currency": "string",
    "channel_response_message": "string",
    "channel_response_code": "string",
    "card_type": "string",
    "bank": "string",
    "approval_code": "string"
  }  

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Body:**
  ```json
  {
    "transaction_time": "string",
    "transaction_status": "string",
    "transaction_id": "string",
    "status_message": "string",
    "status_code": "string",
    "signature_key": "string",
    "payment_type": "string",
    "order_id": "string",
    "merchant_id": "string",
    "masked_card": "string",
    "gross_amount": "string",
    "fraud_status": "string",
    "eci": "string",
    "currency": "string",
    "channel_response_message": "string",
    "channel_response_code": "string",
    "card_type": "string",
    "bank": "string",
    "approval_code": "string"
  }

### POST /api/v1/orders/{orderId}/cancel

This endpoint allows users to cancel an existing order. The order can only be canceled if it has not yet been completed or is not in a non-cancellable status. When an order is successfully canceled, its status will be updated to CANCEL.

#### Request

- **URL:** `/api/v1/orders/{orderId}/cancel`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Path Parameter**
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
    "data": null
    "errors": null
  }

### DELETE /api/v1/orders/{orderId}

This endpoint allows admin to delete an existing order.

#### Request

- **URL:** `/api/v1/orders/{orderId}`
- **ROLE** `ADMIN`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Path Parameter**
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
    "data": null
    "errors": null
  }

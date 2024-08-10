# Order API Documentation

## Overview

The Order API offers endpoints for managing user orders within the Task Master application. This documentation focuses on the order management functionality provided by the `OrderController`.

## Endpoints

### GET /api/v1/orders/{userId}

This endpoint is used for admin to get any user order items

#### Request

- **URL:** `/api/v1/orders/{userId}`
- **ROLE** `ADMIN`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Body:**
  ```text
  userId(String) = 9
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

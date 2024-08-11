# Coupon API DocumentationCouponCart API Documentation

## Overview

The Coupon API provides endpoints for `Role_Admin` to manage coupons within the Task Master application. This documentation focuses on the coupon management functionality offered by the `CouponController`.

## Endpoints

### POST /api/v1/coupons

This endpoint allows administrators to create a new coupon in the system. The coupon can be used by users to apply discounts or special offers during their purchases.

#### Request

- **URL:** `/api/v1/coupons`
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
    "code": "MERDEKA17",
    "discount": 100,
    "expiredAt": "2024-08-18 06:00:00" Time Format: YYYY-MM-DD HH:MM:SS
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
      "code": "MERDEKA17",
      "discount": 100,
      "createdAt": "2024-08-09 21:12:16",
      "expiredAt": "2024-08-18 06:00:00"
    },
    "errors": null
  }

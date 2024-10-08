# Question and Answer API Documentation

## Overview

The Question and Answer API provides endpoints for managing questions and answers within the Task Master application. This documentation focuses on the functionality offered by the `QuestionAndAnswerController` for handling questions and answers.

## Endpoints

### GET /api/v1/qna/answers/me/studies/{studyId}

This endpoint allows users to get their answer, question explanation, and grade. 

#### Request

- **URL:** `/api/v1/qna/answers/me/studies/{studyId}`
- **ROLE** `USER`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
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
      "userScore": "50",
      "explanationList": [
        {
          "questionId": "20",
          "questionText": "Diberikan deret angka berikut: 2, 5, 8, 11, __, 17. Temukan angka yang hilang pada deret tersebut.",
          "explanation": "Pola pada deret ini adalah penambahan 3 setiap suku. Jadi, setelah 11, angka berikutnya adalah 14.",
          "imageUrl": "deret1.jpg",
          "answerId": 57,
          "answerText": "11",
          "correct": false
        },
        {
          "questionId": "21",
          "questionText": "Diberikan deret angka berikut: 3, 9, 27, __, 243. Temukan angka yang hilang pada deret tersebut.",
          "explanation": "Pola pada deret ini adalah perkalian dengan 3. Jadi, setelah 27, angka berikutnya adalah 27 × 3 = 81.",
          "imageUrl": "deret2.jpg",
          "answerId": 61,
          "answerText": "81",
          "correct": true
        }
      ]
    },
    "errors": null
  }

### GET /api/v1/qna/questions/me/studies/{studyId}

This endpoint allows the current authenticated user to get questions for a specific study identified by the studyId.

#### Request

- **URL:** `/api/v1/qna/questions/me/studies/{studyId}`
- **ROLE** `USER`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Parameter:**
    - **Request Variable**
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
    "data": [
      {
        "studyId": 14,
        "questionId": 19,
        "questionText": "Diberikan deret angka berikut: 2, 5, 8, 11, __, 17. Temukan angka yang hilang pada deret tersebut.",
        "imageUrl": "deret1.jpg",
        "answers": [
          {
            "answerId": 56,
            "answerText": "14"
          },
          {
            "answerId": 57,
            "answerText": "11"
          },
          {
            "answerId": 58,
            "answerText": "10"
          },
          {
            "answerId": 59,
            "answerText": "13"
          },
          {
            "answerId": 60,
            "answerText": "12"
          }
        ]
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 2,
      "totalElement": 2,
      "size": 1,
      "empty": false,
      "first": true,
      "last": false
    }
  }

### GET /api/v1/qna/questions/studies/{studyId}

This endpoint allows an admin to retrieve all questions for a specific study identified by the studyId. The response includes detailed information about each question, including any associated answers.

#### Request

- **URL:** `/api/v1/qna/questions/studies/{studyId}`
- **ROLE** `ADMIN`
- **Method:** `GET`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Path Parameter:**
  ```text
  studyId(int) = 24
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
        "studyId": 14,
        "questionId": 19,
        "questionText": "Diberikan deret angka berikut: 2, 5, 8, 11, __, 17. Temukan angka yang hilang pada deret tersebut.",
        "imageUrl": "deret1.jpg",
        "explanation": "Pola pada deret ini adalah penambahan 3 setiap suku. Jadi, setelah 11, angka berikutnya adalah 14.",
        "answers": [
          {
            "answerId": 56,
            "answerText": "14",
            "correct": true
          },
          {
            "answerId": 57,
            "answerText": "11",
            "correct": false
          },
          {
            "answerId": 58,
            "answerText": "10",
            "correct": false
          },
          {
            "answerId": 59,
            "answerText": "13",
            "correct": false
          },
          {
            "answerId": 60,
            "answerText": "12",
            "correct": false
          }
        ]
      }
    ],
    "errors": null,
    "paging": {
      "currentPage": 0,
      "totalPage": 2,
      "totalElement": 2,
      "size": 1,
      "empty": false,
      "first": true,
      "last": false
    }
  }

### POST /api/v1/qna/questions

This endpoint allows an admin to create a new question for a specific study. The request body should include the question text, associated answers, and any relevant details such as explanations. This endpoint is restricted to admin users.

#### Request

- **URL:** `/api/v1/qna/questions`
- **ROLE** `ADMIN`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Body:**
    ```json
    {
      "studyId": 0,
      "questionText": "string",
      "imageUrl": "string",
      "explanation": "string",
      "answers": [
        {
          "answerText": "string",
          "isCorrect": true
        }
      ]
    }  

#### Response
- **Status Code: `200 OK`**
- **Content-Type:** `application/json`
- **Response Headers:**
- **Response Body:**
    ```json
    {
      "code": 200,
      "message": "OK",
      "data": [
        {
          "studyId": 24,
          "id": 11,
          "questionText": "Pilihlah kata yang benar antara Apotek dan Apotik ?",
          "imageUrl": "string",
          "explanation": "karena, Arti kata apotek menurut KBBI Online: apotek / apo·tek / /apoték/ n toko tempat meramu dan menjual obat berdasarkan resep dokter serta memperdagangkan barang medis",
          "answers": [
            {
              "answerId": 45,
              "answerText": "Apotek",
              "correct": true
            },
            {
              "answerId": 46,
              "answerText": "Apotik",
              "correct": false
            }
          ]
        }
      ],
      "errors": null
    }

### POST /api/v1/qna/studies/{studyId}/grade

This endpoint allows an admin to submit the grade for a specific study identified by studyId.

#### Request

- **URL:** `/api/v1/qna/studies/{studyId}/grade`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Query Parameter:**
    ```text
    studyId(int) = 24
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
        "studyCode": "INDO.10.00001",
        "score": 0,
        "gradedAt": "2024-08-11 00:18:14"
      },
      "errors": null
    }

### POST /api/v1/qna/studies/{studyId}/submission

This endpoint allows a user to submit their answers for a specific study identified by studyId. The submission typically includes the user's answers to the questions within the study. This endpoint is restricted to authenticated users.

#### Request

- **URL:** `/api/v1/qna/studies/{studyId}/submission`
- **ROLE** `USER`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Path Parameter:**
    ```text
    studyId(int) = 24
    ```
- **Request Body:**
    ```json
    {
      "questionId": int,
      "answerId": int
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

### PATCH /api/v1/qna/questions/{questionId}

This endpoint allows an Admin to update a question and linked answers identified by the questionId. It is not necessary to update all properties, only properties that include in request will be updated.

#### Request

- **URL:** `/api/v1/qna/questions/{questionId}`
- **ROLE** `ADMIN`
- **Method:** `PATCH`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Query Parameter:**
    ```text
    questionId(int) = 18
    ```
- **Request Body:**
  ```json
  {
    "imageUrl": "gambar.jpg",
    "explanation": "karena 5 + 6 = 11"
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
      "questionText": "5 + 6 ?",
      "imageUrl": "gambar.jpg",
      "explanation": "karena 5 + 6 = 11",
      "createdAt": "2024-08-13 14:51:48",
      "updatedAt": "2024-08-13 17:25:15",
      "answerRequests": [
        {
          "answerId": 55,
          "answerText": "5",
          "isCorrect": false
        },
        {
          "answerId": 54,
          "answerText": "11",
          "isCorrect": true
        }
      ]
    },
    "errors": null
  }

### DELETE /api/v1/qna/questions/{questionId}

This endpoint allows admins to delete questions from studies based on questionId. This deletion will be done to all derivatives of questions such as answers and also user answers.

#### Request

- **URL:** `/api/v1/qna/questions/{questionId}`
- **ROLE** `ADMIN`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Query Parameter:**
    ```text
    questionId(int) = 9
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

### DELETE /api/v1/qna/answers/me/studies/{studyId}

This endpoint allows users to delete their answers based on studyId. This deletion will be done to all derivatives of questions such as user grade.

#### Request

- **URL:** `/api/v1/qna/answers/me/studies/{studyId}`
- **ROLE** `USER`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
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
    "data": null,
    "errors": null
  }

### DELETE /api/v1/qna/answers/{studyId}/users/{userId}

This endpoint allows admins to delete user answers based on studyId and userId. This deletion will be done to all derivatives of questions such as user grade.

#### Request

- **URL:** `/api/v1/qna/answers/{studyId}/users/{userId}`
- **ROLE** `ADMIN`
- **Method:** `DELETE`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
  ```
- **Request Query Parameter:**
    ```text
    studyId(int) = 14
    userId(int) = 15
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

### PATCH /api/v1/qna/answers/me/studies/{studyId}/revision

This endpoint allows users to update their answers based on the provided studyId. Only the specified answers in the request will be updated, rather than updating all user answers at once.

#### Request

- **URL:** `/api/v1/qna/answers/me/studies/{studyId}/revision`
- **ROLE** `USER`
- **Method:** `PATCH`
- **Content-Type:** `application/json`
- **Request Headers:**
  ```text
  authorization : Bearer {your_jwt_token}
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
      "studyId": 14,
      "userAnswerList": [
        {
          "questionId": 20,
          "answerId": 61
        },
        {
          "questionId": 19,
          "answerId": 57
        }
      ]
    },
    "errors": null
  }

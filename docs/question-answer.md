# Question and Answer API Documentation

## Overview

The Question and Answer API provides endpoints for managing questions and answers within the Task Master application. This documentation focuses on the functionality offered by the `QuestionAndAnswerController` for handling questions and answers.

## Endpoints

### GET /api/v1/qna/answers/study/{studyId}

This endpoint allows users to get their answer, question explanation, and grade. 

#### Request

- **URL:** `/api/v1/qna/answers/study/{studyId}`
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
            "userScore": "100",
            "explanationList": [
              {
                "questionId": "4",
                "questionText": "2 + 2 ?",
                "explanation": "karena 2 + 2 adalah 4",
                "imageUrl": "image.jpeg",
                "answerId": 1,
                "answerText": "4",
                "correct": true
              },
              {
                "questionId": "5",
                "questionText": "3 + 3 ?",
                "explanation": "karena 3 ditambah 3 adalah 6",
                "imageUrl": "image3.jpg",
                "answerId": 33,
                "answerText": "6",
                "correct": true
              },
              {
                "questionId": "6",
                "questionText": "5 - 4 ?",
                "explanation": "karena 5 dikurang dengan 4 adalah 1",
                "imageUrl": "image4.jpg",
                "answerId": 39,
                "answerText": "1",
                "correct": true
              }
            ]
          },
          "errors": null
        }

### GET /api/v1/qna/me/questions/{studyId}

This endpoint allows the current authenticated user to get questions for a specific study identified by the studyId.

#### Request

- **URL:** `/api/v1/qna/answers/study/{studyId}`
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
          "data": [
            {
              "studyId": 24,
              "questionId": 10,
              "questionText": "Pilih lah kata yang benar antara Teknologi dengan Tekhnologi?",
              "imageUrl": null,
              "answers": [
                {
                  "answerId": 43,
                  "answerText": "Teknologi"
                },
                {
                  "answerId": 44,
                  "answerText": "Tekhnologi"
                }
              ]
            }
          ],
          "errors": null,
          "paging": {
            "currentPage": 0,
            "totalPage": 1,
            "totalElement": 1,
            "size": 10,
            "empty": false,
            "first": true,
            "last": true
          }
        }

### GET /api/v1/qna/questions/{studyId}

This endpoint allows an admin to retrieve all questions for a specific study identified by the studyId. The response includes detailed information about each question, including any associated answers.

#### Request

- **URL:** `/api/v1/qna/questions/{studyId}`
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
              "studyId": 24,
              "questionId": 10,
              "questionText": "Pilih lah kata yang benar antara Teknologi dengan Tekhnologi?",
              "imageUrl": null,
              "answers": [
                {
                  "answerId": 43,
                  "answerText": "Teknologi"
                },
                {
                  "answerId": 44,
                  "answerText": "Tekhnologi"
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

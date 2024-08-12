# Task Master

## Overview

`Task Master` is an educational platform designed for students who want to understand across various subjects through interactive quizzes and detailed explanations. Task Master backend project develop using `Java 8`, `Spring Boot 2.7.16`, and `PostgreSQL`, Task Master following to the `Model-View-Controller (MVC)` design pattern, ensuring a clear separation of concerns between data, user interface, and control logic.

The platform implements `Role-Based Access Control (RBAC)` to manage user permissions effectively, allowing different levels of access and functionality depending on the user's role, such as `USER`, or `ADMIN`. This ensures a secure and scalable system where each user interacts with the platform according to their specific permissions.

Task Master provides a wide range of services through its `RESTful API`, which follows REST principles to enable smooth communication between the client and server. This architecture supports the platform’s ability to deliver interactive and dynamic educational content.

## Configuration

### Application Configuration

1. **Database Configuration**:
    - Open `src/main/resources/application.properties`
    - Set your PostgreSQL connection details:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password

2. **JWT Secret Key**:
   - Make sure your secret key is at least `32 bytes` long for security and compatible with the `HS256 algorithm`.
   - Set your JWT Secret Key detail:
   ```properties
   jwt.secret-key=your_jwt_secret_key

3. **Midtrans Server Key**:
   - You can obtain this server key once you have an account with Midtrans as a merchant.
   - Go to `Midtrans web app` > `login as merchant` > `sandbox environment` > `setting` > `access key`
   - Set your Midtrans Server Key detail:
   ```properties
   midtrans.server-key=your_midtrans_server_key
   midtrans.is-production=false

4. **Build Steps**:
    - Clone the repository:

    ```bash
    git clone https://github.com/DiffaAzkhani/task-master.git
    
   cd task-master
    ```

    - Install dependencies and build the application:

    ```bash
    mvn clean install
    ```

    - Run the application:

    ```bash
    mvn spring-boot:run
    ```
5.**Access the Application**
   - Once the application is running, it will be accessible at http://localhost:8080. 
   - You can interact with the API using tools like Postman or [Swagger](http://localhost:8080/swagger-ui/index.html#/)
   - Considering that it has not been deployed on the server yet, this application can only be run locally

## API Documentation
Detailed API documentation is available in the `docs/` folder. Below are the links to specific sections:
- [Authentication Docs](docs/authentication.md)
- [User Docs](docs/user.md)
- [Study Docs](docs/study.md)
- [Question and Answer Docs](docs/question-answer.md)
- [Order Docs](docs/order.md)
- [Coupon Docs](docs/coupon.md)
- [Cart Docs](docs/cart.md)

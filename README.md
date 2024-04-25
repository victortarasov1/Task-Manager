**Description:**

This project implements a RESTful API for managing tasks. It allows users to perform CRUD operations (Create, Read, Update, Delete) on tasks efficiently. The API is designed to provide a straightforward way to manage tasks and interact with them through HTTP requests.

### Endpoints

---
You can also explore the documentation for the endpoints by running the application and visiting the following URL: http://localhost:8080/swagger-ui/index.html.

You can also test the endpoints using the Postman collection provided in the postman directory.
#### Retrieve Tasks

- **URL:** `/tasks`
- **Method:** `GET`
- **Description:** Retrieves a list of tasks optionally filtered by title, description, and status.
- **Response Codes:**
    - `200 OK`: Successfully retrieved tasks
- **Response Body:**
  ```json
  [
    {
      "id": 1,
      "title": "Task 1",
      "description": "Description of Task 1",
      "status": "pending"
    },
    {
      "id": 2,
      "title": "Task 2",
      "description": "Description of Task 2",
      "status": "COMPLETED"
    }
  ]
- **Example Requests:**
    - Retrieve all tasks: `GET /tasks`
    - Retrieve tasks filtered by title: `GET /tasks?title=Task%201`
    - Retrieve tasks filtered by status: `GET /tasks?status=COMPLETED`
    - Retrieve tasks filtered by description: `GET /tasks?description=Description%20of%20Task%202`
  - Retrieve tasks filtered by all parameters: `GET /tasks?title=Task%201&description=Description%20of%20Task%202&status=COMPLETED`
#### Find Task by ID

- **URL:** `/tasks/{id}`
- **Method:** `GET`
- **Summary:** Find task by ID
- **Description:** Retrieves a task by its unique identifier.
- **Responses:**
    - `200 OK`: Task found
        - **Response Body:**
          ```json
          {
            "id": 1,
            "title": "Task 1",
            "description": "Description of Task 1",
            "status": "PENDING"
          }
          ```
    - `409 Conflict`: Task not found by ID
    - `400 Bad Request`: Id type mismatch

#### Create a New Task

- **URL:** `/tasks`
- **Method:** `POST`
- **Summary:** Create a new task
- **Description:** Creates a new task based on the provided data.
- **Request Body:**
  ```json
  {
    "title": "New Task",
    "description": "Description of the new task",
    "status": "IN_PROGRESS"
  }
- **Responses:**
- `201 Created`: The task was created successfully.
- `400 Bad Request`: If the request body is invalid.


#### Update Task by ID

- **URL:** `/tasks/{id}`
- **Method:** `PUT`
- **Summary:** Update task by ID
- **Description:** Updates an existing task with the provided data.
- **Path Parameters:**
    - `id`: The unique identifier of the task to update.
- **Request Body:**
  ```json
  {
    "title": "Updated Task",
    "description": "Updated description of the task",
    "status": "COMPLETED"
  }
- **Responses:**
    - `200 OK`: The task was updated successfully.
    - `409 Conflict`: Task not found by ID
    - `400 Bad Request`: Invalid request body


#### Delete Task by ID

- **URL:** `/tasks/{id}`
- **Method:** `DELETE`
- **Summary:** Delete task by ID
- **Description:** Deletes a task by its unique identifier.
- **Path Parameters:**
    - `id`: The unique identifier of the task to delete.
- **Responses:**
    - `200 OK`: Task deleted successfully
    - `409 Conflict`: Task not found by ID
    - `400 Bad Request`: Id type mismatch



### Used Technologies

- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Web
- Springdoc OpenAPI
- Lombok
- Google Jib
- MySQL
- JUnit 5
- AssertJ
- H2 Database
- Mockito

### Running Instructions
#### With Docker
1. Navigate to the docker-compose directory.
2. Execute the command `docker compose up -d`.

#### Without Docker 
If running without Docker, you need to configure MySQL with the following settings:

- **Database Name:** `tasks-db`
- **Username:** `root`
- **Password:** `root`

If you want to use a different configuration, edit the application.yml file accordingly.

Next run: `gradle bootRun`
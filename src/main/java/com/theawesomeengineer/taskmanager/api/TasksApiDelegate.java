package com.theawesomeengineer.taskmanager.api;

import com.theawesomeengineer.taskmanager.model.Error;
import com.theawesomeengineer.taskmanager.model.Task;
import com.theawesomeengineer.taskmanager.model.TaskRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link TasksApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-28T12:34:46.383096376+07:00[Asia/Jakarta]", comments = "Generator version: 7.16.0")
public interface TasksApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /tasks : Create a new task
     * Create a new task with the provided information
     *
     * @param taskRequest  (required)
     * @return Task created successfully (status code 201)
     *         or Invalid input (status code 400)
     *         or Internal server error (status code 500)
     * @see TasksApi#createTask
     */
    default ResponseEntity<Task> createTask(TaskRequest taskRequest) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"createdAt\" : \"2024-01-01T10:00:00Z\", \"description\" : \"Write comprehensive documentation for the task management API\", \"id\" : 1, \"completed\" : false, \"title\" : \"Complete project documentation\", \"updatedAt\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * DELETE /tasks/{id} : Delete task by ID
     * Delete a specific task by its ID
     *
     * @param id Task ID (required)
     * @return Task deleted successfully (status code 204)
     *         or Task not found (status code 404)
     *         or Internal server error (status code 500)
     * @see TasksApi#deleteTask
     */
    default ResponseEntity<Void> deleteTask(Long id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /tasks : Get all tasks
     * Retrieve a list of all tasks
     *
     * @return List of tasks retrieved successfully (status code 200)
     *         or Internal server error (status code 500)
     * @see TasksApi#getAllTasks
     */
    default ResponseEntity<List<Task>> getAllTasks() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"createdAt\" : \"2024-01-01T10:00:00Z\", \"description\" : \"Write comprehensive documentation for the task management API\", \"id\" : 1, \"completed\" : false, \"title\" : \"Complete project documentation\", \"updatedAt\" : \"2024-01-01T10:00:00Z\" }, { \"createdAt\" : \"2024-01-01T10:00:00Z\", \"description\" : \"Write comprehensive documentation for the task management API\", \"id\" : 1, \"completed\" : false, \"title\" : \"Complete project documentation\", \"updatedAt\" : \"2024-01-01T10:00:00Z\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /tasks/{id} : Get task by ID
     * Retrieve a specific task by its ID
     *
     * @param id Task ID (required)
     * @return Task retrieved successfully (status code 200)
     *         or Task not found (status code 404)
     *         or Internal server error (status code 500)
     * @see TasksApi#getTaskById
     */
    default ResponseEntity<Task> getTaskById(Long id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"createdAt\" : \"2024-01-01T10:00:00Z\", \"description\" : \"Write comprehensive documentation for the task management API\", \"id\" : 1, \"completed\" : false, \"title\" : \"Complete project documentation\", \"updatedAt\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PUT /tasks/{id} : Update task by ID
     * Update an existing task with the provided information
     *
     * @param id Task ID (required)
     * @param taskRequest  (required)
     * @return Task updated successfully (status code 200)
     *         or Invalid input (status code 400)
     *         or Task not found (status code 404)
     *         or Internal server error (status code 500)
     * @see TasksApi#updateTask
     */
    default ResponseEntity<Task> updateTask(Long id,
        TaskRequest taskRequest) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"createdAt\" : \"2024-01-01T10:00:00Z\", \"description\" : \"Write comprehensive documentation for the task management API\", \"id\" : 1, \"completed\" : false, \"title\" : \"Complete project documentation\", \"updatedAt\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"details\" : \"Task with ID 123 does not exist\", \"message\" : \"Task not found\", \"timestamp\" : \"2024-01-01T10:00:00Z\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

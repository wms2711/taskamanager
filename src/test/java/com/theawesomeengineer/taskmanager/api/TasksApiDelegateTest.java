// src/test/java/com/theawesomeengineer/taskmanager/api/TasksApiDelegateTest.java
package com.theawesomeengineer.taskmanager.api;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import com.theawesomeengineer.taskmanager.model.Task;
import com.theawesomeengineer.taskmanager.model.TaskRequest;

class TasksApiDelegateTest {

    private final TasksApiDelegate delegate = new TasksApiDelegate() {};

    @Test
    void getRequest_returnsEmptyOptionalByDefault() {
        Optional<NativeWebRequest> result = delegate.getRequest();
        assertThat(result).isEmpty();
    }

    @Test
    void createTask_returnsNotImplemented() {
        TaskRequest request = new TaskRequest("title", "desc");
        ResponseEntity<Task> response = delegate.createTask(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }

    @Test
    void deleteTask_returnsNotImplemented() {
        ResponseEntity<Void> response = delegate.deleteTask(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }

    @Test
    void getAllTasks_returnsNotImplemented() {
        ResponseEntity<List<Task>> response = delegate.getAllTasks();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }

    @Test
    void getTaskById_returnsNotImplemented() {
        ResponseEntity<Task> response = delegate.getTaskById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }

    @Test
    void updateTask_returnsNotImplemented() {
        TaskRequest request = new TaskRequest("updated", "new desc");
        ResponseEntity<Task> response = delegate.updateTask(1L, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }
}
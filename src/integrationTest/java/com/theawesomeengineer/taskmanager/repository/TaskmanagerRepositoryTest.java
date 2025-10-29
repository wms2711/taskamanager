package com.theawesomeengineer.taskmanager;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.theawesomeengineer.taskmanager.model.Task;
import com.theawesomeengineer.taskmanager.model.TaskRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TaskmanagerApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("taskdb")
            .withUsername("taskuser")
            .withPassword("taskpass")
            .withLabel("app", "taskmanager-test");

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    TestRestTemplate rest;

    private TaskRequest buildRequest(String title, String desc, Boolean completed) {
        TaskRequest rq = new TaskRequest();
        rq.setTitle(title);
        rq.setDescription(desc);
        rq.setCompleted(completed);
        return rq;
    }

    @BeforeAll
    static void beforeAll() {
        assertThat(mysql.isRunning()).isTrue();
        System.out.println("MySQL IS RUNNING at: " + mysql.getJdbcUrl());
    }

    @Test
    void contextLoads() {
        assertThat(rest).isNotNull();
    }

    @Test
    void createAndFetchTask() {
        TaskRequest request = buildRequest("Integration test", "via Testcontainers", false);

        ResponseEntity<Task> createResponse = rest.postForEntity("/tasks", request, Task.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task created = createResponse.getBody();

        ResponseEntity<Task> getResponse = rest.getForEntity("/tasks/" + created.getId(), Task.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task fetched = getResponse.getBody();

        assertThat(fetched)
            .usingRecursiveComparison()
            .ignoringFields("createdAt", "updatedAt")
            .isEqualTo(created);

        assertThat(fetched.getCreatedAt())
            .isCloseTo(created.getCreatedAt(), within(1, ChronoUnit.SECONDS));
        assertThat(fetched.getUpdatedAt())
            .isCloseTo(created.getUpdatedAt(), within(1, ChronoUnit.SECONDS));
    }

    // FIXED: Use List<Map> to avoid OffsetDateTime parsing
    @Test
    void listAllTasks() {
        rest.postForEntity("/tasks", buildRequest("A", "desc", true), Task.class);
        rest.postForEntity("/tasks", buildRequest("B", "desc", false), Task.class);

        ResponseEntity<List> listResp = rest.getForEntity("/tasks", List.class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResp.getBody()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void updateTask() {
        Task created = rest.postForEntity("/tasks",
                buildRequest("before", "desc", false), Task.class).getBody();

        TaskRequest update = buildRequest("after", "new desc", true);
        RequestEntity<TaskRequest> req = RequestEntity
                .put(URI.create("/tasks/" + created.getId()))
                .body(update);
        ResponseEntity<Task> putResp = rest.exchange(req, Task.class);
        assertThat(putResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task updated = putResp.getBody();
        assertThat(updated.getTitle()).isEqualTo("after");
        assertThat(updated.getCompleted()).isTrue();
    }

    @Test
    void deleteTask() {
        Long id = Objects.requireNonNull(
                rest.postForEntity("/tasks", buildRequest("to delete", "desc", false), Task.class)
                    .getBody()).getId();

        rest.delete("/tasks/{id}", id);

        ResponseEntity<String> getAfter = rest.getForEntity("/tasks/{id}", String.class, id);
        assertThat(getAfter.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createTaskWithInvalidPayloadReturns400() {
        String invalidJson = "{ invalid json }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<String> response = rest.postForEntity("/tasks", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createTaskWithNullTitleReturns400() {
        TaskRequest rq = new TaskRequest();
        rq.setTitle(null);
        rq.setDescription("desc");
        rq.setCompleted(false);
        ResponseEntity<String> resp = rest.postForEntity("/tasks", rq, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getTaskByNonExistentIdReturns404() {
        ResponseEntity<Void> resp = rest.getForEntity("/tasks/{id}", Void.class, 9999L);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNull();
    }

    @Test
    void updateNonExistentTaskReturns404() {
        TaskRequest rq = buildRequest("ghost", "no such task", false);
        RequestEntity<TaskRequest> req = RequestEntity
                .put(URI.create("/tasks/8888"))
                .body(rq);
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteNonExistentTaskReturns404() {
        rest.delete("/tasks/{id}", 7777L);
        ResponseEntity<String> resp = rest.getForEntity("/tasks/{id}", String.class, 7777L);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void patchIsNotSupportedReturns405() {
        Long taskId = 1L;
        String patchJson = "{\"title\": \"Patched\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchJson, headers);

        ResponseEntity<Void> response = rest.exchange(
            "/tasks/" + taskId,
            HttpMethod.PATCH,
            entity,
            Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void createTaskWithMaxLengthFields() {
        String maxTitle = "X".repeat(255);
        String maxDesc = "Y".repeat(1000);
        TaskRequest rq = buildRequest(maxTitle, maxDesc, true);
        ResponseEntity<Task> resp = rest.postForEntity("/tasks", rq, Task.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task body = resp.getBody();
        assertThat(body.getTitle()).hasSize(255);
        assertThat(body.getDescription()).hasSize(1000);
    }
}
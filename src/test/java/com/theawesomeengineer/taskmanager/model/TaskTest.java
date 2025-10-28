// src/test/java/com/theawesomeengineer/taskmanager/model/TaskTest.java
package com.theawesomeengineer.taskmanager.model;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class TaskTest {

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2025-01-01T10:00:00Z");

    @Test
    void testFullConstructorAndGetters() {
        Task task = new Task(1L, "Write docs", "API documentation", true, NOW, NOW);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Write docs");
        assertThat(task.getDescription()).isEqualTo("API documentation");
        assertThat(task.getCompleted()).isTrue();
        assertThat(task.getCreatedAt()).isEqualTo(NOW);
        assertThat(task.getUpdatedAt()).isEqualTo(NOW);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Task task = new Task();
        task.setId(2L);
        task.setTitle("Test");
        task.setDescription("Unit test");
        task.setCompleted(false);
        task.setCreatedAt(NOW);
        task.setUpdatedAt(NOW);

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo("Test");
        assertThat(task.getDescription()).isEqualTo("Unit test");
        assertThat(task.getCompleted()).isFalse();
        assertThat(task.getCreatedAt()).isEqualTo(NOW);
        assertThat(task.getUpdatedAt()).isEqualTo(NOW);
    }

    @Test
    void testFluentSetters() {
        Task task = new Task()
                .id(3L)
                .title("Fluent")
                .description("Chaining")
                .completed(true)
                .createdAt(NOW)
                .updatedAt(NOW);

        assertThat(task.getId()).isEqualTo(3L);
        assertThat(task.getTitle()).isEqualTo("Fluent");
        assertThat(task.getCompleted()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        Task task1 = new Task(1L, "A", "desc", false, NOW, NOW);
        Task task2 = new Task(1L, "A", "desc", false, NOW, NOW);
        Task task3 = new Task(2L, "B", "desc", true, NOW, NOW);

        assertThat(task1)
            .isEqualTo(task2)
            .hasSameHashCodeAs(task2)
            .isNotEqualTo(task3);
    }

    @Test
    void testToString() {
        Task task = new Task(1L, "Test", "desc", false, NOW, NOW);
        String str = task.toString();

        assertThat(str)
            .contains("class Task")
            .contains("id: 1")
            .contains("title: Test")
            .contains("description: desc")
            .contains("completed: false")
            .contains("createdAt: 2025-01-01T10:00Z")
            .contains("updatedAt: 2025-01-01T10:00Z");
    }

    @Test
    void testToIndentedStringHandlesNull() {
        Task task = new Task();
        task.setDescription(null); // Ensure null handling

        String str = task.toString();
        assertThat(str).contains("description: null");
    }
}
// src/test/java/com/theawesomeengineer/taskmanager/model/TaskRequestTest.java
package com.theawesomeengineer.taskmanager.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class TaskRequestTest {

    @Test
    void testRequiredConstructorAndGetters() {
        TaskRequest req = new TaskRequest("Write docs", "API details");

        assertThat(req.getTitle()).isEqualTo("Write docs");
        assertThat(req.getDescription()).isEqualTo("API details");
        assertThat(req.getCompleted()).isFalse(); // default
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        TaskRequest req = new TaskRequest();
        req.setTitle("Test");
        req.setDescription("Unit test");
        req.setCompleted(true);

        assertThat(req.getTitle()).isEqualTo("Test");
        assertThat(req.getDescription()).isEqualTo("Unit test");
        assertThat(req.getCompleted()).isTrue();
    }

    @Test
    void testFluentSetters() {
        TaskRequest req = new TaskRequest()
                .title("Fluent")
                .description("Chaining")
                .completed(true);

        assertThat(req.getTitle()).isEqualTo("Fluent");
        assertThat(req.getDescription()).isEqualTo("Chaining");
        assertThat(req.getCompleted()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        TaskRequest r1 = new TaskRequest("A", "desc").completed(true);
        TaskRequest r2 = new TaskRequest("A", "desc").completed(true);
        TaskRequest r3 = new TaskRequest("B", "desc");

        assertThat(r1)
            .isEqualTo(r2)
            .hasSameHashCodeAs(r2)
            .isNotEqualTo(r3);
    }

    @Test
    void testToString() {
        TaskRequest req = new TaskRequest("Test", "desc").completed(false);
        String str = req.toString();

        assertThat(str)
            .contains("class TaskRequest")
            .contains("title: Test")
            .contains("description: desc")
            .contains("completed: false");
    }

    @Test
    void testToIndentedStringHandlesNull() {
        TaskRequest req = new TaskRequest();
        req.setTitle(null);
        req.setDescription(null);

        String str = req.toString();
        assertThat(str)
            .contains("title: null")
            .contains("description: null");
    }
}
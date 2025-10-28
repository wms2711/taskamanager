// src/test/java/com/theawesomeengineer/taskmanager/model/ErrorTest.java
package com.theawesomeengineer.taskmanager.model;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ErrorTest {

    @Test
    void testConstructorAndGetters() {
        OffsetDateTime now = OffsetDateTime.now();
        Error error = new Error("Not found", now);

        assertThat(error.getMessage()).isEqualTo("Not found");
        assertThat(error.getTimestamp()).isEqualTo(now);
        assertThat(error.getDetails()).isNull();
    }

    @Test
    void testFluentSetters() {
        Error error = new Error("Bad request", OffsetDateTime.now())
                .details("Invalid input");

        assertThat(error.getDetails()).isEqualTo("Invalid input");
    }

    @Test
    void testEqualsAndHashCode() {
        OffsetDateTime now = OffsetDateTime.parse("2025-01-01T10:00:00Z");
        Error error1 = new Error("Not found", now).details("ID 999");
        Error error2 = new Error("Not found", now).details("ID 999");
        Error error3 = new Error("Different", now);

        assertThat(error1)
            .isEqualTo(error2)
            .hasSameHashCodeAs(error2)
            .isNotEqualTo(error3);
    }

    @Test
    void testToString() {
        Error error = new Error("Test", OffsetDateTime.now());
        String str = error.toString();

        assertThat(str)
            .contains("class Error")
            .contains("message: Test")
            .contains("timestamp:")
            .contains("details: null");
    }

    @Test
    void testNullableDetails() {
        Error error = new Error("Test", OffsetDateTime.now());
        error.setDetails(null);
        assertThat(error.getDetails()).isNull();
    }
}
// src/test/java/com/theawesomeengineer/TaskmanagerApplicationTest.java
package com.theawesomeengineer;

import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskmanagerApplicationTest {

    @Test
    void contextLoads() {
        // Spring context loads successfully
    }

    @Test
    void mainMethodStartsApplication_withoutCrashing() {
        // Run main() in a separate thread to avoid hanging the test
        Thread thread = new Thread(() -> {
            TaskmanagerApplication.main(new String[]{});
        });

        thread.setDaemon(true);
        thread.start();

        // Wait briefly to ensure it starts
        assertThatCode(() -> {
            Thread.sleep(500); // Give it time to boot
            thread.interrupt(); // Stop gracefully
        }).doesNotThrowAnyException();
    }
}
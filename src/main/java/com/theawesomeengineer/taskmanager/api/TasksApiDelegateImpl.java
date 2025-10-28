package com.theawesomeengineer.taskmanager.api;

import com.theawesomeengineer.taskmanager.model.*;
import com.theawesomeengineer.taskmanager.repository.TaskEntity;
import com.theawesomeengineer.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TasksApiDelegateImpl implements TasksApiDelegate {

    private final TaskRepository repo;

    /* ---------- helpers ---------- */
    private TaskEntity findOrThrow(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    private Task map(TaskEntity e) {
        return new Task(e.getId(), e.getTitle(), e.getDescription(), e.getCompleted(),
                        e.getCreatedAt(), e.getUpdatedAt());
    }

    private TaskEntity map(TaskRequest r) {
        return TaskEntity.builder()
                         .title(r.getTitle())
                         .description(r.getDescription())
                         .completed(r.getCompleted() != null && r.getCompleted())
                         .build();
    }

    /* ---------- API methods ---------- */

    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> list = repo.findAll()
                              .stream()
                              .map(this::map)
                              .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<Task> createTask(TaskRequest taskRequest) {
        TaskEntity saved = repo.save(map(taskRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(map(saved));
    }

    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        return ResponseEntity.ok(map(findOrThrow(id)));
    }

    @Override
    public ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) {
        TaskEntity existing = findOrThrow(id);
        existing.setTitle(taskRequest.getTitle());
        existing.setDescription(taskRequest.getDescription());
        existing.setCompleted(taskRequest.getCompleted() != null && taskRequest.getCompleted());
        existing.setUpdatedAt(OffsetDateTime.now());
        TaskEntity updated = repo.save(existing);
        return ResponseEntity.ok(map(updated));
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
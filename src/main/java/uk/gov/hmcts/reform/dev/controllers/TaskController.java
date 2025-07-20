package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.TaskRepository;
import uk.gov.hmcts.reform.dev.models.Task;

import java.util.List;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        Task task = taskRepository.findById(Long.parseLong(id)).orElse(null);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/task/{id}/status")
    public ResponseEntity<Task> createTask(@PathVariable String id, @RequestBody() String status) {
        Task task = taskRepository.findById(Long.parseLong(id)).orElse(null);
        if (task != null) {
            task.setStatus(status);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}

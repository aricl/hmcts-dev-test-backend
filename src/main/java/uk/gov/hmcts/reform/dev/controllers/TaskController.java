package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.TaskRepository;
import uk.gov.hmcts.reform.dev.models.Task;

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
        Task task = taskRepository.findById(Long.parseLong(id)).get();
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}

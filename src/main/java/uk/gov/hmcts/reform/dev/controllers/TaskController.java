package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.TaskRepository;
import uk.gov.hmcts.reform.dev.models.Task;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> createTask(@PathVariable Long id, @RequestParam("taskStatus") String status) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setStatus(status);
            taskRepository.save(task);
        } else {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

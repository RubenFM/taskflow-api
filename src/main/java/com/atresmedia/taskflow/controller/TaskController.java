package com.atresmedia.taskflow.controller;

import java.util.List;

import com.atresmedia.taskflow.model.Task;
import com.atresmedia.taskflow.service.TaskService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> all() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task one(@PathVariable Long id) {
        return taskService.getTasksById(id);
    }

    @PostMapping
    public Task create(@RequestBody Task newTask) {
        return taskService.createTask(newTask);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task newTask) {
        return taskService.updateTask(id, newTask);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}

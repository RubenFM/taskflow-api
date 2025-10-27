package com.taskflow.repository;

import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void save_ShouldPersistTask() {
        Task result = taskRepository.save(task);

        assertNotNull(result.getId());
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void findById_ShouldReturnTask_WhenTaskExists() {
        Task savedTask = taskRepository.save(task);

        Optional<Task> result = taskRepository.findById(savedTask.getId());

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTaskNotExists() {
        Optional<Task> result = taskRepository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllTasks() {
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);
        taskRepository.save(task2);

        List<Task> result = taskRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete_ShouldRemoveTask(){
        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();

        taskRepository.deleteById(taskId);

        Optional<Task> result = taskRepository.findById(taskId);
        assertFalse(result.isPresent());
        assertEquals(0, taskRepository.findAll().size());
    }

    @Test
    void findByStatus_ShouldReturnTasksWithStatus() {
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);  // PENDING
        taskRepository.save(task2); // IN_PROGRESS

        List<Task> pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING);

        assertEquals(1, pendingTasks.size());
        assertEquals(TaskStatus.PENDING, pendingTasks.get(0).getStatus());
    }
}
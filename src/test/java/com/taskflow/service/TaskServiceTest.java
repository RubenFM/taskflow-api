package com.taskflow.service;

import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("test task");
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks_WhenTasksAreFound() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        // When
        List<Task> result = taskService.getAllTasks();
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskIsFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(task.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskIsNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(999L));

        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void createTask_ShouldReturnTask_WhenTaskIsCreated() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals(task.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldUpdateTaskFields_WhenTaskExists() {
        // Given
        Task updatedData = new Task();
        updatedData.setTitle("Updated task");
        updatedData.setDescription("Updated description");
        updatedData.setStatus(TaskStatus.COMPLETED);

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old task");
        existingTask.setDescription("Old description");
        existingTask.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Task result = taskService.updateTask(1L, updatedData);

        // Then
        assertNotNull(result);
        assertEquals("Updated task", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_WhenTaskNotExists_ShouldThrowException() {
        // Given
        Task updatedData = new Task();
        updatedData.setTitle("Updated task");
        updatedData.setDescription("Updated description");
        updatedData.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(999L, updatedData);
        });

        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_WhenTaskNotExists_ShouldThrowException() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(999L);
        });
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
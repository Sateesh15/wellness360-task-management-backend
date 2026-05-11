package com.wellness360.taskmanagement.service;

import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.exception.TaskNotFoundException;
import com.wellness360.taskmanagement.model.Task;
import com.wellness360.taskmanagement.model.TaskStatus;
import com.wellness360.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies retrieval of all tasks.
     *
     * Fetching task lists is a core feature
     * in task management systems.
     *
     * This ensures:
     * 1. Repository returns tasks correctly
     * 2. Entity-to-DTO mapping works properly
     * 3. Response contains expected data
     */
    @Test
    void shouldReturnAllTasks() {

        Task task = Task.create(
                "Learn Spring Boot",
                "Practice REST APIs",
                LocalDate.now()
        );

        when(taskRepository.findAll())
                .thenReturn(List.of(task));

        List<TaskDto.Response> responses =
                taskService.getAllTasks();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Learn Spring Boot",
                responses.get(0).getTitle());

        verify(taskRepository, times(1))
                .findAll();
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies successful retrieval
     * of a task by ID.
     *
     * Applications often fetch single task details.
     * Incorrect retrieval can break task viewing screens.
     *
     * This ensures proper repository lookup and mapping.
     */
    @Test
    void shouldReturnTaskById() {

        Task task = Task.create(
                "Task Title",
                "Task Description",
                LocalDate.now()
        );

        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        TaskDto.Response response =
                taskService.getTaskById(task.getId());

        assertNotNull(response);
        assertEquals(task.getId(), response.getId());
        assertEquals("Task Title", response.getTitle());

        verify(taskRepository, times(1))
                .findById(task.getId());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies exception handling
     * when task does not exist.
     *
     * Applications must return proper errors
     * for invalid task IDs.
     *
     * This ensures TaskNotFoundException
     * is thrown correctly.
     */
    @Test
    void shouldThrowExceptionWhenTaskNotFound() {

        when(taskRepository.findById("invalid-id"))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById("invalid-id")
        );

        verify(taskRepository, times(1))
                .findById("invalid-id");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies successful task creation.
     *
     * Task creation is a primary business feature.
     *
     * This ensures:
     * 1. Request data is processed correctly
     * 2. Repository save method is called
     * 3. Response contains saved task details
     */
    @Test
    void shouldCreateTaskSuccessfully() {

        TaskDto.CreateRequest request =
                new TaskDto.CreateRequest();

        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setDueDate(LocalDate.now());

        Task savedTask = Task.create(
                "New Task",
                "New Description",
                request.getDueDate()
        );

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskDto.Response response =
                taskService.createTask(request);

        assertNotNull(response);
        assertEquals("New Task", response.getTitle());

        verify(taskRepository, times(1))
                .save(any(Task.class));
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies task updates work properly.
     *
     * Updating tasks is essential for modifying
     * titles, descriptions, due dates, and statuses.
     *
     * This ensures partial updates behave correctly.
     */
    @Test
    void shouldUpdateTaskSuccessfully() {

        Task existingTask = Task.create(
                "Old Title",
                "Old Description",
                LocalDate.now()
        );

        TaskDto.UpdateRequest request =
                new TaskDto.UpdateRequest();

        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(existingTask.getId()))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(existingTask);

        TaskDto.Response response =
                taskService.updateTask(existingTask.getId(), request);

        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Description",
                response.getDescription());

        verify(taskRepository, times(1))
                .save(existingTask);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies delete functionality.
     *
     * Users should be able to remove tasks safely.
     *
     * This ensures repository delete method
     * is executed correctly.
     */
    @Test
    void shouldDeleteTaskSuccessfully() {

        when(taskRepository.deleteById("task-id"))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                taskService.deleteTask("task-id"));

        verify(taskRepository, times(1))
                .deleteById("task-id");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies exception handling
     * during delete operations.
     *
     * Deleting non-existing tasks should
     * return proper errors.
     *
     * This prevents silent failures.
     */
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTask() {

        when(taskRepository.deleteById("invalid-id"))
                .thenReturn(false);

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask("invalid-id")
        );

        verify(taskRepository, times(1))
                .deleteById("invalid-id");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies task completion logic.
     *
     * Marking tasks as completed is a major
     * business workflow feature.
     *
     * This ensures status changes correctly
     * to COMPLETED.
     */
    @Test
    void shouldMarkTaskAsCompleted() {

        Task task = Task.create(
                "Complete Task",
                "Description",
                LocalDate.now()
        );

        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskDto.Response response =
                taskService.markAsComplete(task.getId());

        assertEquals(
                TaskStatus.COMPLETED.getValue(),
                response.getStatus()
        );

        verify(taskRepository, times(1))
                .save(task);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies title trimming logic.
     *
     * Users may accidentally add extra spaces
     * in task titles.
     *
     * This ensures clean data storage.
     */
    @Test
    void shouldTrimTaskTitleBeforeSaving() {

        TaskDto.CreateRequest request =
                new TaskDto.CreateRequest();

        request.setTitle("   Clean Title   ");
        request.setDescription("Description");

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        Task task = Task.create(
                "Clean Title",
                "Description",
                null
        );

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        taskService.createTask(request);

        verify(taskRepository).save(taskCaptor.capture());

        assertEquals(
                "Clean Title",
                taskCaptor.getValue().getTitle()
        );
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies partial updates.
     *
     * Applications often update only one field
     * instead of entire objects.
     *
     * This ensures unchanged fields remain intact.
     */
    @Test
    void shouldUpdateOnlyProvidedFields() {

        Task existingTask = Task.create(
                "Original Title",
                "Original Description",
                LocalDate.now()
        );

        TaskDto.UpdateRequest request =
                new TaskDto.UpdateRequest();

        request.setTitle("Updated Title");

        when(taskRepository.findById(existingTask.getId()))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(existingTask);

        TaskDto.Response response =
                taskService.updateTask(existingTask.getId(), request);

        assertEquals("Updated Title", response.getTitle());

        assertEquals(
                "Original Description",
                response.getDescription()
        );
    }
}
package dev.nyura.kamaz.task.service;

import dev.nyura.kamaz.task.dto.TaskCreateDto;
import dev.nyura.kamaz.task.dto.TaskDto;
import dev.nyura.kamaz.task.dto.TaskUpdateDto;
import dev.nyura.kamaz.task.entity.Task;
import dev.nyura.kamaz.task.entity.TaskStatus;
import dev.nyura.kamaz.task.exception.TaskNotFoundException;
import dev.nyura.kamaz.task.mapper.TaskMapper;
import dev.nyura.kamaz.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public List<TaskDto> getUserTasks(String username) {
        return taskRepository.findByOwnerOrAssignee(username, username).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getUserAssignedTasks(String username) {
        return taskRepository.findByAssignee(username).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getUserOwnedTasks(String username) {
        return taskRepository.findByOwner(username).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        validateAccess(task, username);
        return taskMapper.toDto(task);
    }

    public TaskDto createTask(TaskCreateDto taskDto, String username) {
        Task task = taskMapper.createEntity(taskDto);
        task.setOwner(username);
        if (Strings.isBlank(taskDto.getAssignee())) {
            task.setStatus(TaskStatus.NEW);
        } else {
            task.setStatus(TaskStatus.ASSIGNED);
        }
        return taskMapper.toDto(taskRepository.save(task));
    }


    public TaskDto assignTask(Long id, String assignee, String owner) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        validateOwnerAccess(task, owner);
        if (userService.getUserFromCache(assignee) == null) {
            throw new IllegalArgumentException("User not found");
        }
        task.setStatus(TaskStatus.ASSIGNED);
        task.setAssignee(assignee);
        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskDto updateTask(Long id, TaskUpdateDto taskDto, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        validateOwnerAccess(task, username);
        taskMapper.updateEntity(taskDto, task);
        if (taskDto.getAssignee().isBlank()) {
            task.setStatus(TaskStatus.NEW);
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskDto setStatus(Long id, TaskStatus status, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        validateAccess(task, username);
        if (status == TaskStatus.NEW) {
            throw new IllegalArgumentException("Wrong status");
        }
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Task is already completed");
        }
        if (status == task.getStatus()) {
            throw new IllegalArgumentException("Task is already in this status");
        }
        task.setStatus(status);
        return taskMapper.toDto(taskRepository.save(task));
    }

    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        validateOwnerAccess(task, username);
        taskRepository.delete(task);
    }

    private void validateOwnerAccess(Task task, String username) {
        if (!task.getOwner().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void validateAccess(Task task, String username) {
        if (!task.getOwner().equals(username) && !task.getAssignee().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}

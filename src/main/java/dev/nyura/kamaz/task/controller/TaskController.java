package dev.nyura.kamaz.task.controller;

import dev.nyura.kamaz.task.dto.TaskCreateDto;
import dev.nyura.kamaz.task.dto.TaskDto;
import dev.nyura.kamaz.task.dto.TaskUpdateDto;
import dev.nyura.kamaz.task.entity.TaskStatus;
import dev.nyura.kamaz.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "Набор операций по управлению задачами.")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(
            summary = "Получить все задачи пользователя"
    )
    public ResponseEntity<List<TaskDto>> getAllTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getUserTasks(authentication.getName()));
    }

    @GetMapping("/owned")
    @Operation(
            summary = "Получить задачи, созданные пользователем"
    )
    public ResponseEntity<List<TaskDto>> getOwnedTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getUserOwnedTasks(authentication.getName()));
    }

    @GetMapping("/assigned")
    @Operation(
            summary = "Получить задачи, назначенные пользователю"
    )
    public ResponseEntity<List<TaskDto>> getAssignedTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getUserAssignedTasks(authentication.getName()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить задачу по идентификатору",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор задачи"),
            }
    )
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(taskService.getTaskById(id, authentication.getName()));
    }

    @PostMapping
    @Operation(
            summary = "Создать новую задачу"
    )
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskCreateDto taskDto, Authentication authentication) {
        return ResponseEntity.ok(taskService.createTask(taskDto, authentication.getName()));
    }

    @PutMapping("/{id}/assign")
    @Operation(
            summary = "Назначить задачу"
    )
    public ResponseEntity<TaskDto> assignTask(@PathVariable Long id, @RequestParam String username, Authentication authentication) {
        return ResponseEntity.ok(taskService.assignTask(id, username, authentication.getName()));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить задачу",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор задачи")
            }
    )
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDto taskDto, Authentication authentication) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto, authentication.getName()));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Обновить статус задачи",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор задачи"),
                    @Parameter(name = "status", description = "Статус задачи")
            }
    )
    public ResponseEntity<TaskDto> updateOwnerTask(@PathVariable Long id, @RequestParam TaskStatus status, Authentication authentication) {
        return ResponseEntity.ok(taskService.setStatus(id, status, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить задачу",
            parameters = {
                    @Parameter(name = "id", description = "Идентификатор задачи")
            }
    )
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        taskService.deleteTask(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

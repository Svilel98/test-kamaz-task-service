package dev.nyura.kamaz.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String name;
    private UserDto owner;
    private UserDto assignee;
    private String status;
    private String createdAt;
    private String updatedAt;
}

package dev.nyura.kamaz.task.dto;

import dev.nyura.kamaz.task.entity.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
}
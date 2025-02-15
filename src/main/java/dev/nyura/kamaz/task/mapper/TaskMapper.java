package dev.nyura.kamaz.task.mapper;

import dev.nyura.kamaz.task.dto.TaskCreateDto;
import dev.nyura.kamaz.task.dto.TaskDto;
import dev.nyura.kamaz.task.dto.TaskUpdateDto;
import dev.nyura.kamaz.task.dto.UserDto;
import dev.nyura.kamaz.task.entity.Task;
import dev.nyura.kamaz.task.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    public static final String N_D = "N/D";
    @Autowired
    private UserService userService;

    public abstract TaskDto toDto(Task task);

    public UserDto getUserFromCache(String username) {
        try {
            return userService.getUserFromCache(username);
        } catch (Exception e) {
            UserDto newUserDto = new UserDto();
            newUserDto.setFirstName(N_D);
            newUserDto.setLastName(N_D);
            newUserDto.setUsername(username);
            return newUserDto;
        }
    }

    public abstract Task createEntity(TaskCreateDto taskDto);

    public abstract void updateEntity(TaskUpdateDto taskDto, @MappingTarget Task task);
}
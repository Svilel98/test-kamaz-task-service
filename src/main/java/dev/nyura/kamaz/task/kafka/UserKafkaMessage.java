package dev.nyura.kamaz.task.kafka;

import dev.nyura.kamaz.task.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKafkaMessage {
    private UserOperation operation;
    private UserDto userData;
}
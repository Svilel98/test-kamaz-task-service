package dev.nyura.kamaz.task.kafka;

import dev.nyura.kamaz.task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserChangedListener {

    private final UserService userCacheService;

    @KafkaListener(topics = "user-changed", groupId = "task-service")
    public void onUserChanged(UserKafkaMessage message) {
        if (message.getOperation() == UserOperation.USER_DELETED) {
            userCacheService.evictUserCache(message.getUserData().getUsername());
            log.info("User deleted: {}", message.getUserData().getUsername());
        } else {
            userCacheService.saveUserToCache(message.getUserData());
            log.info("User saved: {}", message.getUserData().getUsername());
        }
    }
}

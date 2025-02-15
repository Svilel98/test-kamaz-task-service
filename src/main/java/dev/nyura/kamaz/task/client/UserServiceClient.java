package dev.nyura.kamaz.task.client;

import dev.nyura.kamaz.task.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${services.user.url:http://localhost:8081}")
public interface UserServiceClient {

    @GetMapping("/api/admin/users/{username}")
    UserDto getUserByUsername(@RequestHeader("Authorization") String token, @PathVariable("username") String username);
}

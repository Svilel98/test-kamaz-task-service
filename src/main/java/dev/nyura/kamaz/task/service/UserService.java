package dev.nyura.kamaz.task.service;

import dev.nyura.kamaz.task.client.UserServiceClient;
import dev.nyura.kamaz.task.dto.UserDto;
import dev.nyura.kamaz.task.security.JwtService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    @Cacheable(value = "users", key = "#username")
    public UserDto getUserFromCache(String username) {
        try {
            return userServiceClient.getUserByUsername("Bearer " + jwtService.generateToken(), username);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("User not found");
        }
    }

    @CachePut(value = "users", key = "#userDto.username")
    public UserDto saveUserToCache(UserDto userDto) {
        return userDto;
    }

    @CacheEvict(value = "users", key = "#username")
    public void evictUserCache(String username) {
    }


}
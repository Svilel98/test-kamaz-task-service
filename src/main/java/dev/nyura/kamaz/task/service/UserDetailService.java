package dev.nyura.kamaz.task.service;

import dev.nyura.kamaz.task.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.getUserFromCache(username);
        return org.springframework.security.core.userdetails.User.
                withUsername(user.getUsername())
                .password("password")
                .roles("USER", "ADMIN")
                .build();
    }


}

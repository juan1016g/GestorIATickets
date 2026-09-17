package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
}
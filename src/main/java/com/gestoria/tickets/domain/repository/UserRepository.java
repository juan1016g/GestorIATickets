package com.gestoria.tickets.domain.repository;

import com.gestoria.tickets.domain.dto.UserDto;
import java.util.Optional;

public interface UserRepository {
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findById(Long id);
    UserDto save(UserDto userDto);
}

package com.gestoria.tickets.domain.repository;

import com.gestoria.tickets.domain.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UserDto> findAll();
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findById(Long id);
    UserDto save(UserDto userDto);
    boolean existsByEmail(String email);
}

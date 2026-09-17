package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.exception.BusinessException;
import com.gestoria.tickets.domain.exception.ResourceNotFoundException;
import com.gestoria.tickets.domain.repository.UserRepository;
import com.gestoria.tickets.persistence.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userDto.getRole() == null) {
            userDto.setRole(Role.USER);
        }

        if (userDto.getRole() != Role.USER && userDto.getRole() != Role.SUPPORT) {
            throw new BusinessException("El rol especificado no está autorizado. Roles válidos: USER, SUPPORT.");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BusinessException("El email ya se encuentra registrado en el sistema.");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll();
    }

}
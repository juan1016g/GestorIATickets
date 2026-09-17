package com.gestoria.tickets.web.controller;

import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.service.UserService;
import com.gestoria.tickets.web.dto.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "2. Usuarios", description = "Operaciones de gestión para Solicitantes y Técnicos de Soporte")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Registrar un usuario", description = "Crea un nuevo usuario en el sistema definiendo su rol (USER o SUPPORT).")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRequest request) {

        UserDto userDto = UserDto.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene el listado completo de usuarios registrados en el sistema.")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Consulta los detalles de un usuario específico mediante su identificador único.")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID único del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
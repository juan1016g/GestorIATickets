package com.gestoria.tickets.web.dto.request;

import com.gestoria.tickets.persistence.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Cuerpo de la petición para el registro de un nuevo usuario")
public class UserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Carlos Técnico")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un formato de email válido")
    @Schema(description = "Correo electrónico corporativo", example = "carlos@empresa.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña de acceso", example = "SecurePass123!")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    @Schema(description = "Rol asignado en el sistema", example = "SUPPORT")
    private Role role;
}
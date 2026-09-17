package com.gestoria.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Cuerpo de la petición para asignar un técnico a un ticket")
public class AssignTechnicianRequest {

    @NotNull(message = "El ID del técnico es obligatorio")
    @JsonProperty("tecnicoId")
    @Schema(description = "ID del usuario con rol SOPORTE a asignar", example = "2")
    private Long tecnicoId;
}
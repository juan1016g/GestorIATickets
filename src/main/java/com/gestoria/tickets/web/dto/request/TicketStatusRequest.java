package com.gestoria.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Cuerpo de la petición para actualizar el estado de un ticket")
public class TicketStatusRequest {

    @NotBlank(message = "El estado es obligatorio")
    @JsonProperty("estado")
    @Schema(description = "Nuevo estado del ticket (ABIERTO, EN_PROCESO, RESUELTO, CERRADO)", example = "EN_PROCESO")
    private String estado;
}
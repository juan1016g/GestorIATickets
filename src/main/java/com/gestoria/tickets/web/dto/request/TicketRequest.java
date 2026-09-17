package com.gestoria.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Cuerpo de la petición para la creación de un nuevo ticket")
public class TicketRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    @JsonProperty("titulo")
    @Schema(description = "Título descriptivo de la incidencia", example = "Falla de red en piso 3")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 20, max = 2000, message = "La descripción debe tener entre 20 y 2000 caracteres")
    @JsonProperty("descripcion")
    @Schema(description = "Detalle completo del problema reportado", example = "No hay acceso a internet vía cable ni wifi desde esta mañana, afecta a varias terminales.")
    private String description;

    @NotNull(message = "El ID del usuario es obligatorio")
    @JsonProperty("usuarioId")
    @Schema(description = "Identificador del usuario que solicita soporte", example = "1")
    private Long requesterId;
}
package com.gestoria.tickets.web.dto.request;

import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @NotNull(message = "La prioridad es obligatoria")
    private Priority priority;

    @NotNull(message = "El ID del usuario solicitante es obligatorio")
    private Long requesterId;
}

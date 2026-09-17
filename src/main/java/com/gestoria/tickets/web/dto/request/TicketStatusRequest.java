package com.gestoria.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatusRequest {
    @NotBlank(message = "El estado es obligatorio")
    @JsonProperty("estado")
    private String estado;
}
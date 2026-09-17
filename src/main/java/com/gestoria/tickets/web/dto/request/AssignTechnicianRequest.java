package com.gestoria.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTechnicianRequest {

    @NotNull(message = "El ID del técnico es obligatorio")
    @JsonProperty("tecnicoId")
    private Long tecnicoId;
}

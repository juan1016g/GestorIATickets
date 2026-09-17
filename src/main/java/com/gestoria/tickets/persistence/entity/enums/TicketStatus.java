package com.gestoria.tickets.persistence.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketStatus {
    OPEN("ABIERTO"),
    IN_PROGRESS("EN PROCESO"),
    RESOLVED("RESUELTO"),
    CLOSED("CERRADO");

    private final String ticketStatusName;

    TicketStatus(String ticketStatusName){
        this.ticketStatusName = ticketStatusName;
    }

    @JsonValue
    public String getTicketStatusName() {
        return this.ticketStatusName;
    }

    @JsonCreator
    public static TicketStatus fromSpanish(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (TicketStatus status : TicketStatus.values()) {
            if (status.ticketStatusName.equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + text);
    }

}

package com.gestoria.tickets.persistence.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {
    LOW("BAJA"),
    MEDIUM("MEDIA"),
    HIGH("ALTA"),
    CRITICAL("CRITICA");

    private final String priorityName;

    Priority(String priorityName){
        this.priorityName = priorityName;
    }

    @JsonValue
    public String getPriorityName() {
        return this.priorityName;
    }

    @JsonCreator
    public static Priority fromSpanish(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (Priority p : Priority.values()) {
            if (p.priorityName.equalsIgnoreCase(text.trim()) || p.name().equalsIgnoreCase(text.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Prioridad no válida: " + text);
    }
}

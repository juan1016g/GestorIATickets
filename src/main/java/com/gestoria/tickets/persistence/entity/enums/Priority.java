package com.gestoria.tickets.persistence.entity.enums;

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
    }}

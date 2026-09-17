package com.gestoria.tickets.persistence.entity.enums;

public enum Priority {
    LOW("BAJA"),
    MEDIUM("MEDIA"),
    HIGH("ALTA"),
    CRITICAL("CRITICA");

    private final String priorityName;

    Priority(String priorityName){
        this.priorityName = priorityName;
    }

    public String getPriorityName() {
        return this.priorityName;
    }}

package com.gestoria.tickets.persistence.entity.enums;

public enum TicketStatus {
    OPEN("ABIERTO"),
    IN_PROGRESS("EN PROCESO"),
    RESOLVED("RESUELTO"),
    CLOSED("CERRADO");

    private final String ticketStatusName;

    TicketStatus(String ticketStatusName){
        this.ticketStatusName = ticketStatusName;
    }

    public String getTicketStatusName() {
        return this.ticketStatusName;
    }}

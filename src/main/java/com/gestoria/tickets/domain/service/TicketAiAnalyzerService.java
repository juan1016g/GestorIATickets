package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;

public interface TicketAiAnalyzerService {
    TicketDto analyzeAndEnrichTicket(TicketDto ticketDto);
}

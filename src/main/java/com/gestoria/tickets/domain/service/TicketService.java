package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import java.util.List;

public interface TicketService {
    TicketDto createTicket(TicketDto ticketDto);
    TicketDto getTicketById(Long id);
    List<TicketDto> getAllTickets();
    TicketDto resolveTicket(Long id);
    void deleteTicket(Long id);
}

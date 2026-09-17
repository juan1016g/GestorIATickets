package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import org.springframework.data.domain.Page;

public interface TicketService {
    TicketDto createTicket(TicketDto ticketDto);
    TicketDto getTicketById(Long id);
    //List<TicketDto> getAllTickets();
    Page<TicketDto> getAllTickets(int page, int size, String sortBy, String sortDirection);
    TicketDto resolveTicket(Long id);
    void deleteTicket(Long id);
}

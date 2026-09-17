package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import org.springframework.data.domain.Page;

public interface TicketService {
    TicketDto createTicket(TicketDto ticketDto);
    TicketDto getTicketById(Long id);
    //List<TicketDto> getAllTickets();
    Page<TicketDto> getAllTickets(int page, int size, String sortBy, String sortDirection, TicketStatus status);
    TicketDto resolveTicket(Long id);
    void deleteTicket(Long id);
    TicketDto updateTicketClassification(Long id, Category category, Priority priority);
}

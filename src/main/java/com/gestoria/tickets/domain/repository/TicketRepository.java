package com.gestoria.tickets.domain.repository;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    List<TicketDto> findAll();
    Optional<TicketDto> findById(Long id);
    TicketDto save(TicketDto ticketDto);
    void delete(Long id);
    boolean existsOpenTicket(String title, Long requesterId, TicketStatus status);
}
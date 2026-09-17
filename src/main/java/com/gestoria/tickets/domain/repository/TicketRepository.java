package com.gestoria.tickets.domain.repository;

import com.gestoria.tickets.domain.dto.TicketDto;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    List<TicketDto> findAll();
    Optional<TicketDto> findById(Long id);
    TicketDto save(TicketDto ticketDto);
    void delete(Long id);
}
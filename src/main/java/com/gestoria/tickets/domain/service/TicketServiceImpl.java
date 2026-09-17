package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.exception.ResourceNotFoundException;
import com.gestoria.tickets.domain.repository.TicketRepository;
import com.gestoria.tickets.domain.repository.UserRepository;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketAiAnalyzerService aiAnalyzerService;

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        UserDto requester = userRepository.findById(ticketDto.getRequester().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario solicitante no encontrado con el ID: " + ticketDto.getRequester().getId()));

        boolean hasDuplicate = ticketRepository.existsOpenTicket(ticketDto.getTitle(), requester.getId(), TicketStatus.OPEN);
        if (hasDuplicate) {
            throw new com.gestoria.tickets.domain.exception.BusinessException("Ya tienes un ticket abierto con este mismo título.");
        }

        ticketDto.setRequester(requester);
        ticketDto.setTicketStatus(TicketStatus.OPEN);
        ticketDto.setIsActive(true);

        TicketDto enrichedTicket = aiAnalyzerService.analyzeAndEnrichTicket(ticketDto);

        return ticketRepository.save(enrichedTicket);
    }

    @Override
    public TicketDto getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el ID:" + id));
    }

//    @Override
//    public List<TicketDto> getAllTickets() {
//        return ticketRepository.findAll();
//    }

    @Override
    public Page<TicketDto> getAllTickets(int page, int size, String sortBy, String sortDirection, TicketStatus status) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ticketRepository.findAll(pageable, status);
    }

    @Override
    public TicketDto resolveTicket(Long id) {
        TicketDto ticket = getTicketById(id);
        ticket.setTicketStatus(TicketStatus.RESOLVED);
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        TicketDto ticket = getTicketById(id);
        ticket.setIsActive(false); // Soft Delete
        ticketRepository.save(ticket);
    }
}

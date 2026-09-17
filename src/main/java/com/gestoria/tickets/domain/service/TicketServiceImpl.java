package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.exception.BusinessException;
import com.gestoria.tickets.domain.exception.ResourceNotFoundException;
import com.gestoria.tickets.domain.repository.TicketRepository;
import com.gestoria.tickets.domain.repository.UserRepository;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
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
            throw new BusinessException("Ya tienes un ticket abierto con este mismo título.");
        }

        ticketDto.setRequester(requester);
        ticketDto.setTicketStatus(TicketStatus.OPEN);
        ticketDto.setIsActive(true);

        TicketDto enrichedTicket;

        try {
            enrichedTicket = aiAnalyzerService.analyzeAndEnrichTicket(ticketDto);
        } catch (Exception e) {
            enrichedTicket = applyFallbackRules(ticketDto);
        }

        return ticketRepository.save(enrichedTicket);
    }

    private TicketDto applyFallbackRules(TicketDto ticket) {
        String content = (ticket.getTitle() + " " + ticket.getDescription()).toLowerCase();

        if (content.contains("internet") || content.contains("wifi") || content.contains("red")) {
            ticket.setCategory(Category.CONNECTIVITY);
        } else if (content.contains("pantalla") || content.contains("teclado") || content.contains("equipo")) {
            ticket.setCategory(Category.HARDWARE);
        } else {
            ticket.setCategory(Category.OTHER);
        }

        if (content.contains("urgente") || content.contains("reunión") || content.contains("bloqueado")) {
            ticket.setPriority(Priority.HIGH);
        } else {
            ticket.setPriority(Priority.MEDIUM);
        }

        ticket.setAiSummary("Clasificación por reglas locales (IA no disponible).");
        ticket.setAiTags(List.of("MANUAL", "FALLBACK"));

        return ticket;
    }

    @Override
    public TicketDto updateTicketClassification(Long id, Category category, Priority priority) {
        TicketDto ticket = getTicketById(id);
        if (category != null) ticket.setCategory(category);
        if (priority != null) ticket.setPriority(priority);
        return ticketRepository.save(ticket);
    }

    @Override
    public TicketDto getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el ID:" + id));
    }

    @Override
    public Page<TicketDto> getAllTickets(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            TicketStatus status,
            Category category,
            Priority priority,
            Long requesterId
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ticketRepository.findAll(pageable, status, category, priority, requesterId);
    }

    @Override
    public TicketDto assignTechnician(Long ticketId, Long technicianId) {
        TicketDto ticket = getTicketById(ticketId);

        UserDto technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + technicianId));

        if (technician.getRole() == null || !technician.getRole().name().equalsIgnoreCase("SUPPORT")) {
            throw new BusinessException("Solo un usuario con rol SOPORTE puede ser asignado como técnico.");
        }

        ticket.setAssignee(technician);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        return ticketRepository.save(ticket);
    }

    @Override
    public TicketDto updateTicketStatus(Long id, TicketStatus status) {
        TicketDto ticket = getTicketById(id);

        boolean isClosing = (status == TicketStatus.RESOLVED || status == TicketStatus.CLOSED);
        if (isClosing && ticket.getAssignee() == null) {
            throw new BusinessException("No se puede resolver o cerrar un ticket que no tiene un técnico asignado.");
        }

        ticket.setTicketStatus(status);
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        getTicketById(id);
        ticketRepository.delete(id);
    }
}
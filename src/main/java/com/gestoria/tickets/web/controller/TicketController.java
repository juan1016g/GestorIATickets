package com.gestoria.tickets.web.controller;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.service.TicketService;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import com.gestoria.tickets.web.dto.request.TicketRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketRequest request) {

        TicketDto ticketDto = TicketDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .priority(request.getPriority())
                .requester(UserDto.builder().id(request.getRequesterId()).build())
                .build();

        TicketDto createdTicket = ticketService.createTicket(ticketDto);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String status
    ){
        TicketStatus enumStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            try {
                enumStatus = TicketStatus.fromSpanish(status);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Estado no válido. Valores permitidos: ABIERTO, EN PROCESO, RESUELTO, CERRADO");
            }
        }

        return ResponseEntity.ok(ticketService.getAllTickets(page, size, sortBy, sortDirection, enumStatus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<TicketDto> resolveTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.resolveTicket(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}

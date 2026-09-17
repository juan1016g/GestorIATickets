package com.gestoria.tickets.web.controller;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.service.TicketService;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import com.gestoria.tickets.web.dto.request.AssignTechnicianRequest;
import com.gestoria.tickets.web.dto.request.TicketRequest;
import com.gestoria.tickets.web.dto.request.TicketStatusRequest;
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
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long requesterId
    ){
        TicketStatus enumStatus = null;
        Category enumCategory = null;
        Priority enumPriority = null;

        if (status != null && !status.trim().isEmpty()) {
            try {
                enumStatus = TicketStatus.fromSpanish(status);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Estado no válido. Valores permitidos: ABIERTO, EN PROCESO, RESUELTO, CERRADO");
            }
        }

        if (category != null && !category.trim().isEmpty()) {
            try {
                enumCategory = Category.fromSpanish(category);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Categoría no válida. Valores permitidos: HARDWARE, SOFTWARE, CONECTIVIDAD, ACCESOS, OTRO");
            }
        }

        if (priority != null && !priority.trim().isEmpty()) {
            try {
                enumPriority = Priority.fromSpanish(priority);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Prioridad no válida. Valores permitidos: BAJA, MEDIA, ALTA, CRITICA");
            }
        }

        return ResponseEntity.ok(ticketService.getAllTickets(
                page, size, sortBy, sortDirection,
                enumStatus, enumCategory, enumPriority, requesterId
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketDto> updateTicketStatus(
            @PathVariable Long id,
            @Valid @RequestBody TicketStatusRequest request) {

        TicketStatus enumStatus;
        try {
            enumStatus = TicketStatus.fromSpanish(request.getEstado());
        } catch (IllegalArgumentException e) {
            throw new com.gestoria.tickets.domain.exception.BusinessException(
                    "Estado no válido. Valores permitidos: ABIERTO, EN_PROCESO, RESUELTO, CERRADO");
        }

        return ResponseEntity.ok(ticketService.updateTicketStatus(id, enumStatus));
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<TicketDto> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequest request) {

        return ResponseEntity.ok(ticketService.assignTechnician(id, request.getTecnicoId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/classification")
    public ResponseEntity<TicketDto> updateClassification(
            @PathVariable Long id,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority) {

        Category enumCategory = null;
        if (category != null && !category.trim().isEmpty()) {
            try {
                enumCategory = Category.fromSpanish(category);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Categoría no válida. Valores permitidos: HARDWARE, SOFTWARE, CONECTIVIDAD, ACCESOS, OTRO");
            }
        }

        Priority enumPriority = null;
        if (priority != null && !priority.trim().isEmpty()) {
            try {
                enumPriority = Priority.fromSpanish(priority);
            } catch (IllegalArgumentException e) {
                throw new com.gestoria.tickets.domain.exception.BusinessException(
                        "Prioridad no válida. Valores permitidos: BAJA, MEDIA, ALTA, CRITICA");
            }
        }

        return ResponseEntity.ok(ticketService.updateTicketClassification(id, enumCategory, enumPriority));
    }

}

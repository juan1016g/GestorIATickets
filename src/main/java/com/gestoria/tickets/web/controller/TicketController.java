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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "1. Tickets", description = "Gestión centralizada de incidencias. Incluye análisis semántico con IA y reglas de negocio.")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Crear Ticket (Clasificación IA)", description = "Registra una nueva incidencia. El sistema delega el análisis semántico a Google Gemini para inferir automáticamente la Categoría, Prioridad y generar un Resumen Ejecutivo. Si la IA falla, se aplica un motor de reglas Fallback.")
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
    @Operation(summary = "Consultar Tickets (Búsqueda Multi-Filtro)", description = "Obtiene una lista paginada de tickets. Soporta filtrado combinado por estado, categoría, prioridad y usuario solicitante.")
    public ResponseEntity<Page<TicketDto>> getAllTickets(
            @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "8") int size,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "createdDate") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (ASC/DESC)") @RequestParam(defaultValue = "ASC") String sortDirection,
            @Parameter(description = "Filtrar por estado (Ej. ABIERTO)") @RequestParam(required = false) String status,
            @Parameter(description = "Filtrar por categoría (Ej. CONECTIVIDAD)") @RequestParam(required = false) String category,
            @Parameter(description = "Filtrar por prioridad (Ej. ALTA)") @RequestParam(required = false) String priority,
            @Parameter(description = "Filtrar por ID de usuario solicitante") @RequestParam(required = false) Long requesterId
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
    @Operation(summary = "Obtener Ticket por ID", description = "Consulta todos los detalles, estado e historial de un ticket específico.")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar Estado del Ticket", description = "Modifica el estado operativo del ticket. Regla de Negocio: No es posible marcar como RESUELTO o CERRADO si no cuenta con un técnico asignado.")
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
    @Operation(summary = "Asignar Técnico", description = "Vincula un responsable de soporte al ticket. El usuario proporcionado debe contar obligatoriamente con el rol SUPPORT.")
    public ResponseEntity<TicketDto> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequest request) {

        return ResponseEntity.ok(ticketService.assignTechnician(id, request.getTecnicoId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Ticket (Soft Delete)", description = "Realiza un borrado lógico (is_active = false) preservando el registro para métricas históricas (SLA) e integridad referencial de base de datos.")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/classification")
    @Operation(summary = "Modificar Clasificación Manualmente", description = "Permite a los administradores o técnicos sobrescribir la categoría y prioridad asignada inicialmente por la Inteligencia Artificial.")
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
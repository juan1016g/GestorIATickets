package com.gestoria.tickets.domain.service;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.domain.exception.BusinessException;
import com.gestoria.tickets.domain.exception.ResourceNotFoundException;
import com.gestoria.tickets.domain.repository.TicketRepository;
import com.gestoria.tickets.domain.repository.UserRepository;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.Role;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketAiAnalyzerService aiAnalyzerService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private UserDto requesterUser;
    private UserDto supportUser;
    private UserDto standardUser;
    private TicketDto baseTicket;

    @BeforeEach
    void setUp() {
        requesterUser = UserDto.builder()
                .id(1L)
                .name("Laura Gómez")
                .email("laura@empresa.com")
                .role(Role.USER)
                .build();

        supportUser = UserDto.builder()
                .id(2L)
                .name("Carlos Técnico")
                .email("carlos@empresa.com")
                .role(Role.SUPPORT)
                .build();

        standardUser = UserDto.builder()
                .id(3L)
                .name("Pedro Empleado")
                .email("pedro@empresa.com")
                .role(Role.USER)
                .build();

        baseTicket = TicketDto.builder()
                .id(10L)
                .title("Problema con el internet")
                .description("No puedo conectarme a la red wifi desde esta mañana")
                .requester(requesterUser)
                .ticketStatus(TicketStatus.OPEN)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Debe crear un ticket y clasificarlo con IA exitosamente")
    void createTicket_Success_WithAiClassification() {
        TicketDto enrichedByAi = TicketDto.builder()
                .id(10L)
                .title(baseTicket.getTitle())
                .description(baseTicket.getDescription())
                .requester(requesterUser)
                .category(Category.CONNECTIVITY)
                .priority(Priority.HIGH)
                .aiSummary("Problema de conexión wifi reportado por el usuario")
                .aiTags(List.of("internet", "conectividad", "red"))
                .ticketStatus(TicketStatus.OPEN)
                .isActive(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(requesterUser));
        when(ticketRepository.existsOpenTicket(anyString(), anyLong(), any())).thenReturn(false);
        when(aiAnalyzerService.analyzeAndEnrichTicket(any(TicketDto.class))).thenReturn(enrichedByAi);
        when(ticketRepository.save(any(TicketDto.class))).thenReturn(enrichedByAi);

        TicketDto result = ticketService.createTicket(baseTicket);

        assertNotNull(result);
        assertEquals(Category.CONNECTIVITY, result.getCategory());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals("Problema de conexión wifi reportado por el usuario", result.getAiSummary());
        verify(aiAnalyzerService, times(1)).analyzeAndEnrichTicket(any(TicketDto.class));
        verify(ticketRepository, times(1)).save(any(TicketDto.class));
    }

    @Test
    @DisplayName("Debe clasificar con fallback local cuando la IA lanza excepción")
    void createTicket_FallbackRules_WhenAiFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(requesterUser));
        when(ticketRepository.existsOpenTicket(anyString(), anyLong(), any())).thenReturn(false);
        when(aiAnalyzerService.analyzeAndEnrichTicket(any(TicketDto.class)))
                .thenThrow(new RuntimeException("Gemini API no disponible"));
        when(ticketRepository.save(any(TicketDto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketDto result = ticketService.createTicket(baseTicket);

        assertNotNull(result);
        assertEquals(Category.CONNECTIVITY, result.getCategory());
        assertEquals("Clasificación por reglas locales (IA no disponible).", result.getAiSummary());
        assertTrue(result.getAiTags().contains("FALLBACK"));
        verify(ticketRepository, times(1)).save(any(TicketDto.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al consultar un ticket inexistente")
    void getTicketById_NotFound_ThrowsException() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(99L));
        verify(ticketRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe rechazar la asignación si el usuario no tiene rol SOPORTE")
    void assignTechnician_InvalidRole_ThrowsException() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(baseTicket));
        when(userRepository.findById(3L)).thenReturn(Optional.of(standardUser));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                ticketService.assignTechnician(10L, 3L)
        );

        assertEquals("Solo un usuario con rol SOPORTE puede ser asignado como técnico.", ex.getMessage());
        verify(ticketRepository, never()).save(any(TicketDto.class));
    }

    @Test
    @DisplayName("Debe asignar exitosamente un técnico con rol SOPORTE")
    void assignTechnician_Success() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(baseTicket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(supportUser));
        when(ticketRepository.save(any(TicketDto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketDto result = ticketService.assignTechnician(10L, 2L);

        assertNotNull(result.getAssignee());
        assertEquals("Carlos Técnico", result.getAssignee().getName());
        assertEquals(Role.SUPPORT, result.getAssignee().getRole());
        verify(ticketRepository, times(1)).save(any(TicketDto.class));
    }

    @Test
    @DisplayName("Debe actualizar el estado cuando el ticket cuenta con técnico asignado")
    void updateTicketStatus_Success() {
        baseTicket.setAssignee(supportUser);
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(baseTicket));
        when(ticketRepository.save(any(TicketDto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketDto result = ticketService.updateTicketStatus(10L, TicketStatus.RESOLVED);

        assertEquals(TicketStatus.RESOLVED, result.getTicketStatus());
        verify(ticketRepository, times(1)).save(baseTicket);
    }

    @Test
    @DisplayName("Debe lanzar excepción al resolver o cerrar un ticket sin técnico asignado")
    void updateTicketStatus_WithoutAssignee_ThrowsException() {
        baseTicket.setAssignee(null);
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(baseTicket));

        assertThrows(BusinessException.class, () ->
                ticketService.updateTicketStatus(10L, TicketStatus.RESOLVED)
        );
        verify(ticketRepository, never()).save(any(TicketDto.class));
    }
}
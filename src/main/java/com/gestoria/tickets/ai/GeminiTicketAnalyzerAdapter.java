package com.gestoria.tickets.ai;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.service.TicketAiAnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiTicketAnalyzerAdapter implements TicketAiAnalyzerService {

    private final GeminiAiAssistant aiAssistant;

    @Override
    public TicketDto analyzeAndEnrichTicket(TicketDto ticketDto) {
        try {
            String summary = aiAssistant.generateSummary(ticketDto.getTitle(), ticketDto.getDescription());
            ticketDto.setAiSummary(summary.trim());

            String tagsResponse = aiAssistant.generateTags(ticketDto.getTitle(), ticketDto.getDescription());

            // Limpiar y formatear las etiquetas generadas por la IA
            List<String> tags = Arrays.stream(tagsResponse.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .toList();
            ticketDto.setAiTags(tags);

        } catch (Exception e) {
            // Fallback seguro: Si la IA falla (ej. sin internet), el ticket se crea sin enriquecer
            System.err.println("Error al conectar con Gemini: " + e.getMessage());
            ticketDto.setAiSummary("Análisis de IA no disponible temporalmente.");
        }

        return ticketDto;
    }
}

package com.gestoria.tickets.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.service.TicketAiAnalyzerService;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiTicketAnalyzerAdapter implements TicketAiAnalyzerService {

    private final GeminiAiAssistant aiAssistant;
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Override
//    public TicketDto analyzeAndEnrichTicket(TicketDto ticketDto) {
//        try {
//            String summary = aiAssistant.generateSummary(ticketDto.getTitle(), ticketDto.getDescription());
//            ticketDto.setAiSummary(summary.trim());
//
//            String tagsResponse = aiAssistant.generateTags(ticketDto.getTitle(), ticketDto.getDescription());
//
//            // Limpiar y formatear las etiquetas generadas por la IA
//            List<String> tags = Arrays.stream(tagsResponse.split(","))
//                    .map(String::trim)
//                    .map(String::toUpperCase)
//                    .toList();
//            ticketDto.setAiTags(tags);
//
//        } catch (Exception e) {
//            // Fallback seguro: Si la IA falla (ej. sin internet), el ticket se crea sin enriquecer
//            System.err.println("Error al conectar con Gemini: " + e.getMessage());
//            ticketDto.setAiSummary("Análisis de IA no disponible temporalmente.");
//        }
//
//        return ticketDto;
//    }

    @Override
    public TicketDto analyzeAndEnrichTicket(TicketDto ticketDto) {
        try {
            String jsonResponse = aiAssistant.analyzeTicketStructure(ticketDto.getTitle(), ticketDto.getDescription());

            jsonResponse = jsonResponse.replace("```json", "").replace("```", "").trim();

            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            ticketDto.setCategory(Category.valueOf(jsonNode.get("category").asText().toUpperCase()));
            ticketDto.setPriority(Priority.valueOf(jsonNode.get("priority").asText().toUpperCase()));
            ticketDto.setAiSummary(jsonNode.get("summary").asText());

            List<String> tags = new ArrayList<>();
            jsonNode.get("tags").forEach(tag -> tags.add(tag.asText().toUpperCase()));
            ticketDto.setAiTags(tags);

            return ticketDto;

        } catch (Exception e) {
            System.err.println("Error procesando respuesta de Gemini: " + e.getMessage());
            throw new RuntimeException("Fallo en la clasificación de IA", e);
        }
    }

}

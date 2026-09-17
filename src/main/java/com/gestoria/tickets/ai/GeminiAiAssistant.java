package com.gestoria.tickets.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface GeminiAiAssistant {

    @SystemMessage("""
            Eres un experto en soporte técnico IT de nivel 3.
            Sé directo y conciso.
            """)
    @UserMessage("""
            Resume el siguiente problema en una sola frase profesional (máximo 150 caracteres).
            Título: {{title}}.
            Descripción: {{description}}
            """)
    String generateSummary(@V("title") String title, @V("description") String description);

    @SystemMessage("""
            Eres un sistema automatizado de clasificación de soporte IT.
            """)
    @UserMessage("""
            Genera exactamente 3 palabras clave (etiquetas) separadas por comas que describan este problema.
            Solo devuelve las palabras, sin viñetas ni explicaciones.
            Título: {{title}}.
            Descripción: {{description}}
            """)
    String generateTags(@V("title") String title, @V("description") String description);

    @SystemMessage("""
            Eres un sistema experto de soporte IT. Analiza el título y descripción provistos.
            Devuelve ÚNICAMENTE un JSON válido con esta estructura exacta, sin texto adicional ni formato Markdown:
            {
              "category": "Una de: HARDWARE, SOFTWARE, NETWORK, OTHER",
              "priority": "Una de: LOW, MEDIUM, HIGH, URGENT",
              "summary": "Resumen técnico en máximo 150 caracteres",
              "tags": ["tag1", "tag2", "tag3"]
            }
            """)
    @UserMessage("""
            Título: {{title}}.
            Descripción: {{description}}
            """)
    String analyzeTicketStructure(@V("title") String title, @V("description") String description);
}

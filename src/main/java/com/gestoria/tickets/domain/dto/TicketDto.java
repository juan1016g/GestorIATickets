package com.gestoria.tickets.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private TicketStatus ticketStatus;
    private UserDto requester;
    private UserDto assignee;
    private String aiSummary;
    private List<String> aiTags;
    private Boolean isActive;
    private LocalDateTime createdDate;

    @JsonIgnore
    private Long version;
}
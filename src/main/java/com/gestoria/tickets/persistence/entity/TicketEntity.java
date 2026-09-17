package com.gestoria.tickets.persistence.entity;

import com.gestoria.tickets.persistence.audit.AuditTicketListener;
import com.gestoria.tickets.persistence.audit.AuditableEntity;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners({AuditTicketListener.class})
public class TicketEntity extends AuditableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    private TicketStatus ticketStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private UserEntity requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @Column(name = "ai_summary", length = 500)
    private String aiSummary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ai_tags", columnDefinition = "jsonb")
    private List<String> aiTags;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Version
    private Long version;

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", priority=" + priority +
                ", ticketStatus=" + ticketStatus +
                ", requesterId=" + (requester != null ? requester.getId() : "null") +
                ", assigneeId=" + (assignee != null ? assignee.getId() : "null") +
                ", aiSummary='" + aiSummary + '\'' +
                ", aiTags=" + aiTags +
                '}';
    }
}
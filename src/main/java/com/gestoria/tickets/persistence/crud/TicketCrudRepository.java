package com.gestoria.tickets.persistence.crud;

import com.gestoria.tickets.persistence.entity.TicketEntity;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TicketCrudRepository extends   JpaRepository<TicketEntity, Long>,
                                                ListPagingAndSortingRepository<TicketEntity, Long> {
    boolean existsByTitleAndRequesterIdAndTicketStatus(String title, Long requesterId, TicketStatus ticketStatus);
    Page<TicketEntity> findByTicketStatus(TicketStatus ticketStatus, Pageable pageable);

    @Query("SELECT t FROM TicketEntity t WHERE " +
            "(:status IS NULL OR t.ticketStatus = :status) AND " +
            "(:category IS NULL OR t.category = :category) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:requesterId IS NULL OR t.requester.id = :requesterId)")
    Page<TicketEntity> findAllByFilters(
            @Param("status") TicketStatus status,
            @Param("category") Category category,
            @Param("priority") Priority priority,
            @Param("requesterId") Long requesterId,
            Pageable pageable);

}

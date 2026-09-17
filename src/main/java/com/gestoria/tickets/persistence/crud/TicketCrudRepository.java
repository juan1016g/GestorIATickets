package com.gestoria.tickets.persistence.crud;

import com.gestoria.tickets.persistence.entity.TicketEntity;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TicketCrudRepository extends   JpaRepository<TicketEntity, Long>,
                                                ListPagingAndSortingRepository<TicketEntity, Long> {
    boolean existsByTitleAndRequesterIdAndTicketStatus(String title, Long requesterId, TicketStatus ticketStatus);
}

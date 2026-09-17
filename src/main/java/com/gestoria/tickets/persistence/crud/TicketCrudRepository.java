package com.gestoria.tickets.persistence.crud;

import com.gestoria.tickets.persistence.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCrudRepository extends JpaRepository<TicketEntity, Long> {
}

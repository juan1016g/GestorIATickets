package com.gestoria.tickets.persistence.repository;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.domain.repository.TicketRepository;
import com.gestoria.tickets.persistence.crud.TicketCrudRepository;
import com.gestoria.tickets.persistence.entity.TicketEntity;
import com.gestoria.tickets.persistence.entity.enums.Category;
import com.gestoria.tickets.persistence.entity.enums.Priority;
import com.gestoria.tickets.persistence.entity.enums.TicketStatus;
import com.gestoria.tickets.persistence.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

    private final TicketCrudRepository crudRepository;
    private final TicketMapper mapper;

    @Override
    public List<TicketDto> findAll() {
        return mapper.toTicketDtos(crudRepository.findAll());
    }

    @Override
    public Page<TicketDto> findAll(
            Pageable pageable,
            TicketStatus status,
            Category category,
            Priority priority,
            Long requesterId
    ) {
        return crudRepository.findAllByFilters(status, category, priority, requesterId, pageable)
                .map(mapper::toTicketDto);
    }

    @Override
    public Optional<TicketDto> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toTicketDto);
    }

    @Override
    public TicketDto save(TicketDto ticketDto) {
        TicketEntity entity = mapper.toTicketEntity(ticketDto);
        return mapper.toTicketDto(crudRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        // Pasar a Soft Delete
        crudRepository.deleteById(id);
    }

    @Override
    public boolean existsOpenTicket(String title, Long requesterId, TicketStatus status) {
        return crudRepository.existsByTitleAndRequesterIdAndTicketStatus(title, requesterId, status);
    }
}
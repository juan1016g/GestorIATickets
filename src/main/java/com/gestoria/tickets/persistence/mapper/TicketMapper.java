package com.gestoria.tickets.persistence.mapper;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.persistence.entity.TicketEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface TicketMapper {
    TicketDto toTicketDto(TicketEntity ticketEntity);
    TicketEntity toTicketEntity(TicketDto ticketDto);
    List<TicketDto> toTicketDtos(List<TicketEntity> ticketEntities);
}

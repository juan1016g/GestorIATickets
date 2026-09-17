package com.gestoria.tickets.persistence.mapper;

import com.gestoria.tickets.domain.dto.TicketDto;
import com.gestoria.tickets.persistence.entity.TicketEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Builder;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class},
        builder = @Builder(disableBuilder = true)
)
public interface TicketMapper {
    TicketDto toTicketDto(TicketEntity ticketEntity);
    List<TicketDto> toTicketDtos(List<TicketEntity> ticketEntities);

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    TicketEntity toTicketEntity(TicketDto ticketDto);
}

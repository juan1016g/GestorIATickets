package com.gestoria.tickets.persistence.mapper;

import com.gestoria.tickets.domain.dto.UserDto;
import com.gestoria.tickets.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(UserEntity userEntity);
    UserEntity toUserEntity(UserDto userDto);
}

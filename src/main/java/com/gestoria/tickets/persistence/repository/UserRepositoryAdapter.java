package com.gestoria.tickets.persistence.repository;

import com.gestoria.tickets.domain.repository.UserRepository;
import com.gestoria.tickets.persistence.crud.UserCrudRepository;
import com.gestoria.tickets.persistence.entity.UserEntity;
import com.gestoria.tickets.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.gestoria.tickets.domain.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserCrudRepository crudRepository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> findAll() {
        return mapper.toUserDtos(crudRepository.findAll());
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return crudRepository.findByEmail(email).map(mapper::toUserDto);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toUserDto);
    }

    @Override
    public UserDto save(UserDto userDto) {
        UserEntity entity = mapper.toUserEntity(userDto);
        return mapper.toUserDto(crudRepository.save(entity));
    }

    @Override
    public boolean existsByEmail(String email) {
        return crudRepository.existsByEmail(email);
    }


}

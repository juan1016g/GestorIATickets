package com.gestoria.tickets.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gestoria.tickets.persistence.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;

    @JsonIgnore
    private String password;

}
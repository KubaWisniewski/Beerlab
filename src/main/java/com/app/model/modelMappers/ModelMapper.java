package com.app.model.modelMappers;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.User;
import com.app.model.dto.BeerDto;
import com.app.model.dto.RoleDto;
import com.app.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ModelMapper {
    public UserDto fromUserToUserDto(User user) {
        return user ==
                null ? null : UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .balance(user.getBalance())
                .username(user.getUsername())
                .rolesDto(user.getRoles() == null ? null : user.getRoles().stream().map(this::fromRoleToRoleDto).collect(Collectors.toSet()))
                .build();
    }

    public User fromUserDtoToUser(UserDto userDto) {
        return userDto ==
                null ? null : User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .balance(userDto.getBalance())
                .username(userDto.getUsername())
                .roles(userDto.getRolesDto() == null ? null : userDto.getRolesDto().stream().map(this::fromRoleDtoToRole).collect(Collectors.toSet()))
                .build();
    }

    public RoleDto fromRoleToRoleDto(Role role) {
        return role ==
                null ? null : RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public Role fromRoleDtoToRole(RoleDto roleDto) {
        return roleDto ==
                null ? null : Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .build();
    }

    public BeerDto fromBeerToBeerDto(Beer beer) {
        return beer ==
                null ? null : BeerDto.builder()
                .id(beer.getId())
                .brand(beer.getBrand())
                .description(beer.getDescription())
                .imgUrl(beer.getImgUrl())
                .price(beer.getPrice())
                .quantity(beer.getQuantity())
                .build();
    }
    public Beer fromBeerDtoToBeer(BeerDto beerDto) {
        return beerDto ==
                null ? null : Beer.builder()
                .id(beerDto.getId())
                .brand(beerDto.getBrand())
                .description(beerDto.getDescription())
                .imgUrl(beerDto.getImgUrl())
                .price(beerDto.getPrice())
                .quantity(beerDto.getQuantity())
                .build();
    }
}

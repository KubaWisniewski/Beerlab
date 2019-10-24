package com.app.model.modelMappers;

import com.app.model.*;
import com.app.model.dto.*;
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

    public OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return orderItem == null ? null : OrderItemDto.builder().beerDto(orderItem.getBeer() == null ? null : fromBeerToBeerDto(orderItem.getBeer())).build();
    }

    public OrderItem fromOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        return orderItemDto == null ? null : OrderItem.builder().beer(orderItemDto.getBeerDto() == null ? null : fromBeerDtoToBeer(orderItemDto.getBeerDto())).build();
    }

    public OrderDto fromOrderToOrderDto(Order order) {
        return order ==
                null ? null : OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderItemsDto(order.getOrderItems() == null ? null : order.getOrderItems().stream().map(this::fromOrderItemToOrderItemDto).collect(Collectors.toSet()))
                .build();
    }

    public Order fromOrderDtoToOrder(OrderDto orderDto) {
        return orderDto
                ==
                null ? null : Order.builder()
                .id(orderDto.getId())
                .status(orderDto.getStatus())
                .orderItems(orderDto.getOrderItemsDto() == null ? null : orderDto.getOrderItemsDto().stream().map(this::fromOrderItemDtoToOrderItem).collect(Collectors.toSet()))
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

    public GroupDto fromGroupToGroupDto(Group group) {
        return group == null ? null : GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .members(group.getMembers() == null ? null : group.getMembers().stream().map(this::fromUserToUserDto).collect(Collectors.toSet()))
                .description(group.getDescription())
                .build();
    }

    public Group fromGroupDtoToGroup(GroupDto groupDto) {
        return groupDto == null ? null : Group.builder()
                .id(groupDto.getId())
                .name(groupDto.getName())
                .description(groupDto.getDescription())
                .members(groupDto.getMembers() == null ? null : groupDto.getMembers().stream().map(this::fromUserDtoToUser).collect(Collectors.toSet()))
                .build();
    }
}

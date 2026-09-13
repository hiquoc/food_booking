package com.huy.food.services;

import com.huy.food.dtos.requests.CreateMenuItemRequest;
import com.huy.food.dtos.requests.UpdateMenuItemRequest;
import com.huy.food.dtos.responses.MenuItemResponse;
import com.huy.food.entities.MenuItem;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    List<MenuItemResponse> getMenuItems();
    MenuItemResponse getMenuItem(Long id);
    MenuItemResponse createMenuItem(CreateMenuItemRequest request);
    MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request);
    void deleteMenuItem(Long id, UUID requestId);

    List<MenuItem> getMenuItemsWithRestaurant(Collection<Long> ids, UUID restaurantId);
}

package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateMenuItemRequest;
import com.huy.food.dtos.requests.UpdateMenuItemRequest;
import com.huy.food.dtos.responses.MenuItemResponse;
import com.huy.food.entities.MenuItem;
import com.huy.food.entities.RestaurantSection;
import com.huy.food.entities.User;
import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.MenuItemMapper;
import com.huy.food.repositories.MenuItemRepository;
import com.huy.food.services.MenuItemService;
import com.huy.food.services.RestaurantSectionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository repository;
    private final RestaurantSectionService sectionService;
    private final MenuItemMapper mapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItems() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItem(Long id) {
        return mapper.toResponse(getMenuItemById(id));
    }

    @Override
    @Transactional
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
        RestaurantSection section = sectionService.getSectionById(request.getSectionId());

        MenuItem menuItem = mapper.toEntity(request);
        menuItem.setRestaurant(section.getRestaurant());
        menuItem.setSection(section);
        long orderIndex=repository.countMenuItemBySectionId(request.getSectionId());
        menuItem.setDisplayOrder(orderIndex*100);

        return mapper.toResponse(repository.save(menuItem));
    }

    @Override
    @Transactional
    public MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = getMenuItemById(id);
        mapper.update(request, menuItem);

        if (request.getSectionId() != null) {
            RestaurantSection section = sectionService.getSectionById(request.getSectionId());
            menuItem.setSection(section);
        }
        if (request.getNewPreviousDisplayOrder() == null) {
            menuItem.setDisplayOrder(request.getNewNextDisplayOrder() / 2);
        } else if (request.getNewNextDisplayOrder() == null) {
            menuItem.setDisplayOrder(request.getNewPreviousDisplayOrder() + 100);

        } else {
            long previous = request.getNewPreviousDisplayOrder();
            long next = request.getNewNextDisplayOrder();
            if (next - previous <= 1) {
                // No integer space between them
                // reorderAllSections(section);
            } else {
                menuItem.setDisplayOrder((previous + next) / 2);
            }
        }

        return mapper.toResponse(repository.save(menuItem));
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id, UUID requesterId) {
        MenuItem menuItem = getMenuItemById(id);
        User user=menuItem.getRestaurant().getOwner();
        if(!user.getId().equals(requesterId)){
            log.warn("Unauthorized access to delete this menu item, requesterId={}",requesterId);
            throw new ForbiddenException("You are not allowed to delete this menu item");
        }
        menuItem.softDelete(null);
        repository.save(menuItem);
    }

    @Override
    public List<MenuItem> getMenuItemsWithRestaurant(Collection<Long> ids, UUID restaurantId) {
        return repository.findByIdInAndRestaurantId(ids,restaurantId);
    }

    private MenuItem getMenuItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu item not found with id: " + id));
    }
}

package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateDeliveryItemRequest;
import com.huy.food.dtos.requests.UpdateDeliveryItemRequest;
import com.huy.food.dtos.responses.DeliveryItemResponse;
import com.huy.food.entities.Delivery;
import com.huy.food.entities.DeliveryItem;
import com.huy.food.entities.MenuItem;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.DeliveryItemMapper;
import com.huy.food.repositories.DeliveryItemRepository;
import com.huy.food.repositories.DeliveryRepository;
import com.huy.food.services.DeliveryItemService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryItemServiceImpl implements DeliveryItemService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryItemRepository itemRepository;
    private final DeliveryItemMapper itemMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryItemResponse> getItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryItemResponse getItem(Long id) {
        return itemMapper.toResponse(findItemById(id));
    }

    @Override
    @Transactional
    public DeliveryItemResponse createItem(CreateDeliveryItemRequest request) {
        Delivery delivery = findDeliveryById(request.getDeliveryId());
        MenuItem menuItem = null;
        if (request.getMenuItemId() != null) {
            menuItem = entityManager.getReference(MenuItem.class, request.getMenuItemId());
        }

        DeliveryItem item=itemMapper.toEntity(request);
        item.setDelivery(delivery);
        item.setMenuItem(menuItem);

        return itemMapper.toResponse(itemRepository.save(item));
    }

    @Override
    @Transactional
    public DeliveryItemResponse updateItem(Long id, UpdateDeliveryItemRequest request) {
        DeliveryItem item = findItemById(id);
        itemMapper.update(request, item);

        if (request.getMenuItemId() != null) {
            MenuItem menuItem = entityManager.getReference(MenuItem.class, request.getMenuItemId());
            item.setMenuItem(menuItem);
        }

        return itemMapper.toResponse(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        DeliveryItem item = findItemById(id);
        item.softDelete(null);
        itemRepository.save(item);
    }

    private Delivery findDeliveryById(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
    }

    private DeliveryItem findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery item not found"));
    }
}


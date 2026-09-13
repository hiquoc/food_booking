package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateDeliveryRequest;
import com.huy.food.dtos.requests.DeliveryMenuItemRequest;
import com.huy.food.dtos.requests.UpdateDeliveryRequest;
import com.huy.food.dtos.responses.DeliveryResponse;
import com.huy.food.entities.*;
import com.huy.food.enums.DeliveryStatus;
import com.huy.food.enums.MenuItemStatus;
import com.huy.food.enums.RestaurantStatus;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.DeliveryMapper;
import com.huy.food.repositories.DeliveryItemRepository;
import com.huy.food.repositories.DeliveryRepository;
import com.huy.food.services.DeliveryService;
import com.huy.food.services.MenuItemService;
import com.huy.food.services.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final EntityManager entityManager;

    private final UserService userService;
    private final MenuItemService menuItemService;
    private final DeliveryItemRepository  deliveryItemRepository;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final int DELIVERY_CAP = 3;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getDelivery(UUID id) {
        return deliveryMapper.toResponse(findDeliveryById(id));
    }

    @Override
    @Transactional
    public DeliveryResponse createDelivery(CreateDeliveryRequest request, UUID userId) {
        validateUser(userId);
        User user = entityManager.getReference(User.class, userId);

        Map<Long, Integer> itemsMap = new HashMap<>();
        Map<Long, Integer> itemsVersionMap = new HashMap<>();
        for (DeliveryMenuItemRequest item : request.getItems()) {
            if(item.getQuantity() <= 0){
                throw new BadRequestException("Item " + item.getItemId() + " quantity must be greater than 0");
            }
            itemsMap.put(item.getItemId(), item.getQuantity());
            itemsVersionMap.put(item.getItemId(), item.getVersion());
        }

        List<MenuItem> menuItems = menuItemService.getMenuItemsWithRestaurant(itemsMap.keySet(), request.getRestaurantId());
        validateMenuItemAndRestaurant(menuItems, request.getItems().size(),itemsVersionMap);

        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setUser(user);
        delivery.setCode(generateDeliveryCode());

        BigDecimal totalOriginalPrice = BigDecimal.ZERO;
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        for (MenuItem item : menuItems) {
            int quantity = itemsMap.get(item.getId());

            BigDecimal unitPrice = calculateUnitPrice(item);
            BigDecimal originalPrice = item.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));
            BigDecimal lineTotal = unitPrice
                    .multiply(BigDecimal.valueOf(quantity));

            totalOriginalPrice = totalOriginalPrice.add(originalPrice);
            totalSubtotal = totalSubtotal.add(lineTotal);

            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setDelivery(delivery);
            deliveryItem.setMenuItem(item);
            deliveryItem.setUnitPrice(unitPrice);
            deliveryItem.setQuantity(quantity);
            deliveryItem.setLineTotal(lineTotal);
            deliveryItem.setItemName(item.getName());
            deliveryItem.setDiscountRate(item.getDiscountRate());
            delivery.getItems().add(deliveryItem);
        }

        BigDecimal discountedAmount = totalOriginalPrice.subtract(totalSubtotal);

        delivery.setSubtotal(totalSubtotal);
        delivery.setDiscountedAmount(discountedAmount);

        //test value
        BigDecimal distance = BigDecimal.valueOf(1000);
        BigDecimal shippingFee = BigDecimal.valueOf(15000.00);
        BigDecimal shipperRevenue = BigDecimal.valueOf(13000.50);
        Instant expectedDeliveryAt = Instant.now().plusSeconds(600);

        delivery.setTotalAmount(totalSubtotal.add(shippingFee));
        delivery.setDistance(distance);
        delivery.setShippingFee(shippingFee);
        delivery.setShipperRevenue(shipperRevenue);
        delivery.setExpectedDeliveryAt(expectedDeliveryAt);

        return deliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional
    public DeliveryResponse updateDelivery(UUID id, UpdateDeliveryRequest request) {
        Delivery d = findDeliveryById(id);
        deliveryMapper.update(request, d);
        return deliveryMapper.toResponse(deliveryRepository.save(d));
    }

    @Override
    @Transactional
    public void deleteDelivery(UUID id) {
        Delivery d = findDeliveryById(id);
        d.softDelete(null);
        deliveryRepository.save(d);
    }

    private Delivery findDeliveryById(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
    }

    private String generateDeliveryCode() {
        String date = LocalDate.now().format(DATE_FORMAT);
        int random = 10_000_000 + RANDOM.nextInt(90_000_000);

        return date + "-" + random;
    }

    private void validateUser(UUID userId) {
        if (userService.checkIfUserIsBanned(userId)) {
            throw new ForbiddenException("You are banned!");
        }
        userReachedMaxDeliveryCapacity(userId);
    }

    private void validateMenuItemAndRestaurant(List<MenuItem> menuItem,long size, Map<Long, Integer> versionMap) {
        if (menuItem.size() != size) {
            throw new BadRequestException("All menu item must came from the same restaurant!");
        }

        Restaurant restaurant = menuItem.getFirst().getRestaurant();
        if (restaurant.getStatus() != RestaurantStatus.ACTIVE) {
            throw new BadRequestException("Restaurant is not active!");
        }
        for (MenuItem item : menuItem) {
            if (item.getStatus() != MenuItemStatus.AVAILABLE) {
                throw new BadRequestException("Item " + item.getName() + " is not available!");
            }
            if(item.getVersion()!=versionMap.get(item.getId())) {
                throw new BadRequestException("Item " + item.getName() + " has changed!");
            }
        }
    }

    private void userReachedMaxDeliveryCapacity(UUID userId) {
        int activeOrder = deliveryRepository.countByUserIdAndStatusNotIn(userId, List.of(DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED));
        if (activeOrder > DELIVERY_CAP) {
            log.warn("User reached max delivery capacity, userId {}", userId);
            throw new ForbiddenException("You can only have " + DELIVERY_CAP + " order at the same time!");
        }
    }

    private BigDecimal calculateSubtotal(MenuItem item, int quantity) {
        BigDecimal price = item.getPrice();
        BigDecimal discountRate = item.getDiscountRate();

        if (discountRate == null || discountRate.signum() == 0) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }

        BigDecimal discountedPrice = price
                .multiply(BigDecimal.valueOf(100).subtract(discountRate))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

        return discountedPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateOriginalPrice(MenuItem item, int quantity) {
        return item.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateUnitPrice(MenuItem item) {
        BigDecimal discountRate = item.getDiscountRate();
        if (discountRate == null || discountRate.signum() == 0) {
            return item.getPrice();
        }

        return item.getPrice()
                .multiply(BigDecimal.valueOf(100).subtract(discountRate))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
    }

}


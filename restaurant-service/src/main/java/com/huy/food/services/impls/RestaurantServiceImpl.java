package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateRestaurantRequest;
import com.huy.food.dtos.requests.UpdateRestaurantRequest;
import com.huy.food.dtos.responses.RestaurantResponse;
import com.huy.food.dtos.responses.UserResponse;
import com.huy.food.entities.Restaurant;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import com.huy.food.enums.RestaurantStatus;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.RestaurantMapper;
import com.huy.food.repositories.RestaurantRepository;
import com.huy.food.services.RestaurantService;
import com.huy.food.services.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final EntityManager entityManager;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantResponse(UUID restaurantId) {
        return restaurantMapper.toResponse(getRestaurantById(restaurantId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurantsByOwner(UUID ownerId) {
        return restaurantRepository.findByOwnerId(ownerId).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request, UUID ownerId) {
        if (ownerId == null) {
            throw new BadRequestException("Owner ID is required to create a restaurant");
        }

        User owner = userService.getUserByIdAndRole(ownerId, AccountRole.OWNER);
        Restaurant restaurant = restaurantMapper.toEntity(request);
        restaurant.setOwner(owner);

        restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public RestaurantResponse updateRestaurant(UUID restaurantId, UpdateRestaurantRequest request, UUID requesterId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        User owner=restaurant.getOwner();
        if(!owner.getId().equals(requesterId)){
            log.warn("Unauthorized access to update this restaurant, requesterId={}",requesterId);
            throw new ForbiddenException("You are not allowed to update this restaurant");
        }
        restaurantMapper.update(request, restaurant);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurant(UUID restaurantId, UUID requesterId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        User owner=restaurant.getOwner();
        if(!owner.getId().equals(requesterId)){
            log.warn("Unauthorized access to delete this restaurant, requesterId={}",requesterId);
            throw new ForbiddenException("You are not allowed to update this restaurant");
        }
        restaurant.softDelete(null);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurants(RestaurantStatus status) {
        if (status == null) {
            status = RestaurantStatus.ACTIVE;
        }
        return restaurantRepository.findByStatus(status).stream().map(restaurantMapper::toResponse).toList();
    }


    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));
    }
}

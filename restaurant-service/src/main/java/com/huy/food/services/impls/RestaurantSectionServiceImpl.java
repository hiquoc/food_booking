package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateRestaurantSectionRequest;
import com.huy.food.dtos.requests.UpdateRestaurantSectionRequest;
import com.huy.food.dtos.responses.RestaurantSectionResponse;
import com.huy.food.entities.Restaurant;
import com.huy.food.entities.RestaurantSection;
import com.huy.food.entities.User;
import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.RestaurantSectionMapper;
import com.huy.food.repositories.RestaurantRepository;
import com.huy.food.repositories.RestaurantSectionRepository;
import com.huy.food.services.RestaurantSectionService;
import com.huy.food.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantSectionServiceImpl implements RestaurantSectionService {
    private final RestaurantSectionRepository repository;
    private final RestaurantService restaurantService;
    private final RestaurantSectionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantSectionResponse> getSectionsResponse() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantSectionResponse getSectionResponse(Long id) {
        return mapper.toResponse(getSectionById(id));
    }

    @Override
    @Transactional
    public RestaurantSectionResponse createSection(CreateRestaurantSectionRequest request) {
        Restaurant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());
        long orderIndex = repository.countRestaurantSectionByRestaurantId(request.getRestaurantId());
        RestaurantSection section = mapper.toEntity(request);
        section.setRestaurant(restaurant);
        section.setDisplayOrder(orderIndex * 100);
        return mapper.toResponse(repository.save(section));
    }

    @Override
    @Transactional
    public RestaurantSectionResponse updateSection(Long id, UpdateRestaurantSectionRequest request, UUID requesterId) {
        RestaurantSection section = getSectionById(id);
        User owner=section.getRestaurant().getOwner();
        if(!owner.getId().equals(requesterId)){
            log.warn("Unauthorized access to update this section, requesterId={}",requesterId);
            throw new ForbiddenException("You are not allowed to update this section");
        }
        mapper.update(request, section);

        if (request.getNewPreviousDisplayOrder() == null) {
            section.setDisplayOrder(request.getNewNextDisplayOrder() / 2);
        } else if (request.getNewNextDisplayOrder() == null) {
            section.setDisplayOrder(request.getNewPreviousDisplayOrder() + 100);

        } else {
            long previous = request.getNewPreviousDisplayOrder();
            long next = request.getNewNextDisplayOrder();
            if (next - previous <= 1) {
                // No integer space between them
                // reorderAllSections(section);
            } else {
                section.setDisplayOrder((previous + next) / 2);
            }
        }

        return mapper.toResponse(repository.save(section));
    }

    @Override
    @Transactional
    public void deleteSection(Long id, UUID requesterId) {
        RestaurantSection section = getSectionById(id);
        User owner=section.getRestaurant().getOwner();
        if(!owner.getId().equals(requesterId)){
            log.warn("Unauthorized access to delete this section, requesterId={}",requesterId);
            throw new ForbiddenException("You are not allowed to delete this section");
        }
        section.softDelete(null);
        repository.save(section);
    }

    @Override
    public RestaurantSection getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant section not found with id " + id));
    }
}

package com.huy.food.services.impls;

import com.huy.food.dtos.requests.CreateOwnerRequest;
import com.huy.food.dtos.requests.UpdateOwnerRequest;
import com.huy.food.dtos.responses.OwnerResponse;
import com.huy.food.entities.Owner;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.OwnerMapper;
import com.huy.food.repositories.OwnerRepository;
import com.huy.food.services.OwnerService;
import com.huy.food.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final UserService userService;
    private final OwnerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<OwnerResponse> getOwners() {
        return ownerRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerResponse getOwner(UUID id) {
        return mapper.toResponse(getOwnerById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerResponse getOwnerByUserId(UUID userId) {
        Owner owner = ownerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Owner not found for user: " + userId));
        return mapper.toResponse(owner);
    }

    @Override
    @Transactional
    public OwnerResponse createOwner(CreateOwnerRequest request) {
        User user = userService.getUserById(request.getUserId());

        if (ownerRepository.existsByUserId(user.getId())) {
            throw new BadRequestException("Owner profile already exists for user: " + user.getId());
        }

        if (user.getRole() == AccountRole.USER) {
            user.setRole(AccountRole.OWNER);
        }

        Owner owner = mapper.toEntity(request);
        owner.setUser(user);

        return mapper.toResponse(ownerRepository.save(owner));
    }

    @Override
    @Transactional
    public OwnerResponse updateOwner(UUID id, UpdateOwnerRequest request) {
        Owner owner = getOwnerById(id);
        mapper.update(request, owner);

        return mapper.toResponse(owner);
    }

    @Override
    @Transactional
    public void deleteOwner(UUID id) {
        Owner owner = getOwnerById(id);
        owner.softDelete(null);
        ownerRepository.save(owner);
    }

    private Owner getOwnerById(UUID id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner not found with id: " + id));
    }
}
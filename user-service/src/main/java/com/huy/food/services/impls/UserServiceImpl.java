package com.huy.food.services.impls;

import com.huy.food.dtos.requests.UpdateUserRequest;
import com.huy.food.dtos.responses.UserResponse;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.ForbiddenException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.UserMapper;
import com.huy.food.repositories.UserRepository;
import com.huy.food.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponse getUserResponse(UUID userId, String phone) {
        User user = getUserById(userId);
        if (user.getRole() != AccountRole.ADMIN || user.getPhone().equals(phone)) {
            return mapper.toResponse(user);
        }
        return phone == null ? mapper.toResponse(user) : mapper.toResponse(getUserByPhone(phone));

    }

    @Override
    public User getUserById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public Optional<User> getUserByPhoneWithOpt(String phone) {
        return repository.findByPhone(phone);
    }

    @Override
    public User getUserByPhone(String phone) {
        return repository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("User not found with phone: " + phone));
    }

    @Override
    public User getUserByIdAndRole(UUID id, AccountRole role) {
        User user = getUserById(id);
        if (user.getRole() != role) {
            log.warn("User role validation failed, userId={}, expectedRole={}, actualRole={}", user.getId(), role, user.getRole());
            throw new NotFoundException("User with id: " + user.getId() + ", role: " + role + " doesn't exist");
        }
        return user;
    }

    @Override
    public boolean checkIfUserIsBanned(UUID userId) {
        return repository.existsByIdAndBanned(userId,true);
    }

    @Override
    public User createUser(String phone, AccountRole roleType) {
        if (roleType != AccountRole.USER) {
            if (roleType == AccountRole.SHIPPER) {
                log.warn("Unauthorized user login shipping application, phone: {}", phone);
                throw new BadRequestException("You are not allowed to login into shipping application.");
            }
        }

        return repository.save(
                User.builder()
                        .id(UUID.randomUUID())
                        .name("User " + phone.substring(phone.length() - 4))
                        .phone(phone)
                        .role(AccountRole.USER)
                        .build()
        );
    }

    @Override
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        User user = getUserById(userId);

        mapper.updateUserFromDto(request, user);
        repository.save(user);
        return mapper.toResponse(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = getUserById(userId);
        repository.delete(user);
    }

    @Override
    public UserResponse updateUserRole(UUID requesterId, UUID userId, AccountRole role) {
        log.info("Updating user role, requesterId={}, userId={}, newRole={}", requesterId, userId, role);

        if (requesterId.equals(userId)) {
            log.warn("Role update forbidden: requester attempted to change own role, requesterId={}", requesterId);
            throw new ForbiddenException("You can't change your own role");
        }

        User requester = getUserById(requesterId);
        if (requester.getRole() != AccountRole.ADMIN) {
            log.warn("Role update forbidden: requester is not admin, requesterId={}, requesterRole={}, targetUserId={}",
                    requesterId, requester.getRole(), userId);
            throw new ForbiddenException("Only ADMIN account can change role");
        }

        User user = getUserById(userId);
        if (user.getRole() == AccountRole.ADMIN) {
            log.warn("Role update forbidden: target user is admin, requesterId={}, targetUserId={}", requesterId, userId);
            throw new ForbiddenException("You can't change admin account role");
        }

        AccountRole oldRole = user.getRole();
        user.setRole(role);
        repository.save(user);

        log.info("User role updated successfully, requesterId={}, userId={}, oldRole={}, newRole={}", requesterId, userId, oldRole, role);

        return mapper.toResponse(user);
    }

}

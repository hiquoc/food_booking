package com.huy.food;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.huy.food.entities.User;
import com.huy.food.entities.Shipper;
import com.huy.food.entities.Owner;
import com.huy.food.entities.Restaurant;
import com.huy.food.enums.AccountRole;
import com.huy.food.repositories.UserRepository;
import com.huy.food.repositories.ShipperRepository;
import com.huy.food.repositories.OwnerRepository;
import com.huy.food.repositories.RestaurantRepository;

@SpringBootTest
@Transactional
public class AuthAndDomainModelTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipperRepository shipperRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testUserCreationAndRoles() {
        // Create a generic user
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .phone("+1234567890")
                .role(AccountRole.USER)
                .build();
        userRepository.save(user);

        // Verify persisted
        User fetched = userRepository.findById(user.getId()).orElseThrow();
        assertThat(fetched.getRole()).isEqualTo(AccountRole.USER);
    }

    @Test
    void testShipperLinkageToUser() {
        // Create a user with SHIPPER role
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Shipper Sam")
                .phone("+1987654321")
                .role(AccountRole.SHIPPER)
                .build();
        userRepository.save(user);

        // Create a shipper linked to the user
        Shipper shipper = Shipper.builder()
                .user(user)
                .build();
        shipperRepository.save(shipper);

        Shipper fetched = shipperRepository.findById(shipper.getId()).orElseThrow();
        assertThat(fetched.getUser()).isNotNull();
        assertThat(fetched.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void testOwnerLinkageToUser() {
        // Create an owner user
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Owner Olivia")
                .phone("+1122334466")
                .role(AccountRole.OWNER)
                .build();
        userRepository.save(owner);

        // Create an owner profile linked to the user (same id via @MapsId)
        Owner ownerProfile = Owner.builder()
                .user(owner)
                .build();
        ownerRepository.save(ownerProfile);

        Owner fetched = ownerRepository.findById(ownerProfile.getId()).orElseThrow();
        assertThat(fetched.getId()).isEqualTo(owner.getId());
        assertThat(fetched.getUser()).isNotNull();
        assertThat(fetched.getUser().getId()).isEqualTo(owner.getId());
    }

    @Test
    void testRestaurantOwnerLinkage() {
        // Create an owner user
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Owner Olivia")
                .phone("+1122334455")
                .role(AccountRole.OWNER)
                .build();
        owner = userRepository.save(owner);

        // Create a restaurant owned by this user
        Restaurant restaurant = Restaurant.builder()
                .name("Olivia's Kitchen")
                .owner(owner)
                .build();
        restaurantRepository.save(restaurant);

        Restaurant fetched = restaurantRepository.findById(restaurant.getId()).orElseThrow();
        assertThat(fetched.getOwner()).isNotNull();
        assertThat(fetched.getOwner().getId()).isEqualTo(owner.getId());
    }
}

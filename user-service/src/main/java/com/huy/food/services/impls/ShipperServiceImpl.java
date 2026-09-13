package com.huy.food.services.impls;

import com.huy.food.dtos.locations.Cell;
import com.huy.food.dtos.requests.CreateShipperRequest;
import com.huy.food.dtos.requests.UpdateShipperRequest;
import com.huy.food.dtos.responses.ShipperResponse;
import com.huy.food.dtos.locations.ShipperLocation;
import com.huy.food.entities.Shipper;
import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import com.huy.food.exceptions.BadRequestException;
import com.huy.food.exceptions.NotFoundException;
import com.huy.food.mappers.ShipperMapper;
import com.huy.food.repositories.ShipperRepository;
import com.huy.food.services.ShipperService;
import com.huy.food.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;
    private final UserService userService;
    private final ShipperMapper mapper;

    private final Map<Cell,List<UUID>> cellMap = new ConcurrentHashMap<>();
    private final Map<UUID, ShipperLocation> shipperLocationMap = new ConcurrentHashMap<>();
    private final double originLat = 10.0;
    private final double originLng = 106.0;
    private final double metersPerLat = 111320.0;
    private final double metersPerLng = metersPerLat * Math.cos(Math.toRadians(originLat));
    private final int CELL_SIZE=500;

    @Override
    @Transactional(readOnly = true)
    public List<ShipperResponse> getShippers() {
        return shipperRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShipperResponse getShipper(UUID id) {
        return mapper.toResponse(getShipperById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ShipperResponse getShipperByUserId(UUID userId) {
        Shipper shipper = shipperRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Shipper not found for user: " + userId));
        return mapper.toResponse(shipper);
    }

    @Override
    @Transactional
    public ShipperResponse createShipper(CreateShipperRequest request) {
        User user = userService.getUserById(request.getUserId());

        if (shipperRepository.existsByUserId(user.getId())) {
            throw new BadRequestException("Shipper profile already exists for user: " + user.getId());
        }

        if (user.getRole() == AccountRole.USER) {
            user.setRole(AccountRole.SHIPPER);
        }

        Shipper shipper = mapper.toEntity(request);
        shipper.setUser(user);

        return mapper.toResponse(shipperRepository.save(shipper));
    }

    @Override
    @Transactional
    public ShipperResponse updateShipper(UUID id, UpdateShipperRequest request) {
        Shipper shipper = getShipperById(id);
        mapper.update(request, shipper);

        return mapper.toResponse(shipper);
    }

    @Override
    @Transactional
    public void deleteShipper(UUID id) {
        Shipper shipper = getShipperById(id);
        shipper.softDelete(null);
        shipperRepository.save(shipper);
    }

    @Override
    public void sendLocation(UUID shipperId, ShipperLocation location) {
        log.info("shipperId={}, location={}", shipperId, location);
        shipperLocationMap.put(shipperId, location);
        log.info("All shipper location");
        shipperLocationMap.forEach((key, value)
                -> log.info("Shipper id {}, location {}", key, shipperLocationMap.get(key)));

        int xMeters = (int) ((location.longitude() - originLng) * metersPerLng);
        int yMeters = (int) ((location.latitude() - originLat) * metersPerLat);
        int cellX = (int) (double) (xMeters / 500);
        int cellY = (int) (double) (yMeters / 500);
        Cell cell = new Cell(cellX, cellY);
    }

    private Shipper getShipperById(UUID id) {
        return shipperRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shipper not found with id: " + id));
    }
}

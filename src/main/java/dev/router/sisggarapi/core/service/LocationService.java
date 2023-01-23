package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface LocationService {
    Location createLocation(Location location);
    Location updateLocation(Location location, UUID locationId);
    void delete(UUID locationId);
    Optional<Location> findById(UUID locationId);
    Optional<Location> findByDescription(String description);
    //Page<Location> getLocationsIntoStorage(Specification<Location> spec, Pageable pageable);
    Page<Location> getLocationsIntoStorage(Pageable pageable);
}

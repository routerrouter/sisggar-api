package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Location;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.LocationRepository;
import dev.router.sisggarapi.core.repository.StorageRepository;
import dev.router.sisggarapi.core.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class LocationServiceImpl implements LocationService {

    final private LocationRepository repository;
    final private StorageRepository storageRepository;

    public LocationServiceImpl(LocationRepository repository,StorageRepository storageRepository) {
        this.repository = repository;
        this.storageRepository = storageRepository;
    }

    @Override
    public Location createLocation(Location location) {
        log.debug("POST saveLocation locationDto received {} ", location.toString());

        var optLocation = repository.findByDescription(location.getDescription());

        if (optLocation.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe localização com esta descrição. Por favor tente cadastrar com outra descrição");
        }

        storageExist(location);
        location.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        location.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        location.setLocationStatus(Status.ACTIVE);
        location = repository.save(location);
        log.debug("POST saveLocation locationId saved {} ", location.getLocationId());
        log.info("Location saved successfully locationId {} ", location.getLocationId());
        return location;
    }


    @Override
    public Location updateLocation(Location location, UUID locationId) {
        log.debug("PUT updateLocation Location received {} ", location.toString());
        var optLocation = repository.findById(locationId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Localidade não encontrada!"));

        optLocation.setDescription(location.getDescription());
        optLocation.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.debug("PUT updateLocation LocationId saved {} ", locationId);
        log.info("Location updated successfully Location {} ", optLocation);
        return createLocation(optLocation);
    }

    @Transactional
    @Override
    public void delete(UUID locationId) {
        log.debug("DELETE deleteLocation locationId received {} ", locationId);
        var optLocation = repository.findById(locationId);
        if (!optLocation.isPresent()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Localidade não encontrada!");
        }
        log.debug("DELETE deleteLocation locationId deleted {} ", locationId);
        log.info("Location deleted successfully locationId {} ", locationId);
        repository.delete(locationId);
    }

    @Override
    public Optional<Location> findById(UUID locationId) {
        Location location = repository.findById(locationId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Localidade não encontrada!"));

        return Optional.of(location);
    }

    @Override
    public Optional<Location> findByDescription(String description) {
        return repository.findByDescription(description);
    }


    @Override
    public Page<Location> getLocationsIntoStorage(UUID storageId,String description, Pageable pageable) {
        return repository.findAll(storageId,description, pageable);
    }

    private void storageExist(Location location) {
        if (location.getStorage() != null) {
            var storageOptional = storageRepository.findById(location.getStorage().getStorageId());
            if (!storageOptional.isPresent() || storageOptional.get().getStorageStatus().equals(Status.INACTIVE)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Armazem informado não existe ou não está ativo. Por favor verificar as informações enviadas");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Armazem não informado ou não existe. Por favor verifique as informações enviadas");
        }
    }
}

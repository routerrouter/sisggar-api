package dev.router.sisggarapi.core.service.impl;


import dev.router.sisggarapi.core.domain.DeliveryManager;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.DeliveryManagerRepository;
import dev.router.sisggarapi.core.repository.StorageRepository;
import dev.router.sisggarapi.core.service.DeliveryManagerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

    private DeliveryManagerRepository managerRepository;
    private final StorageRepository storageRepository;

    DeliveryManagerServiceImpl(DeliveryManagerRepository managerRepository, StorageRepository storageRepository) {
        this.managerRepository = managerRepository;
        this.storageRepository = storageRepository;
    }


    @Override
    public DeliveryManager createDeliveryManager(DeliveryManager deliveryManager) {
        log.debug("POST saveDeliveryManager deliveryManager received {} ", deliveryManager.toString());

        var deliveryManagerModelOptional = managerRepository.findByPhoneNumber(deliveryManager.getPhoneNumber());
        if (deliveryManagerModelOptional.isPresent()) {
            new ResponseStatusException(HttpStatus.CONFLICT, "O número de telefone informado já existe!");
        }

        if (deliveryManager.getStorage() != null) {
            var storage = storageRepository.findById(deliveryManager.getStorage().getStorageId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Storage not exist or is not active!"));
        }

        deliveryManager.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        deliveryManager.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        deliveryManager.setStatus(Status.ACTIVE);
        deliveryManager = managerRepository.save(deliveryManager);
        log.debug("POST saveStorage storageId saved {} ", deliveryManager.getDeliveryManagerId());
        log.info("Storage saved successfully storageId {} ", deliveryManager.getDeliveryManagerId());
        return deliveryManager;
    }

    @Transactional
    @Override
    public DeliveryManager updateDeliveryManager(DeliveryManager deliveryManager, UUID deliveryId) {
        log.debug("PUT updateDeliveryManager DeliveryManagerDto received {} ", deliveryManager.toString());
        var optDeliveryManager = managerRepository.findById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DeliveryManager Not Found"));

        var storageOptional = storageRepository.findById(deliveryId);
        if (!storageOptional.isPresent() || storageOptional.get().getStorageStatus().equals(Status.INACTIVE)) {
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Storage not exist or is not active!");
        }

        optDeliveryManager.setPhoneNumber(deliveryManager.getPhoneNumber());
        optDeliveryManager.setFullName(deliveryManager.getFullName());
        optDeliveryManager.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        optDeliveryManager = managerRepository.save(optDeliveryManager);
        log.debug("PUT updateDeliveryManager deliveryManagerId saved {} ", optDeliveryManager.getDeliveryManagerId());
        log.info("DeliveryManager updated successfully deliveryManagerId {} ", optDeliveryManager.getDeliveryManagerId());
        return optDeliveryManager;
    }

    @Transactional
    @Override
    public void delete(UUID deliveryManagerId) {
        log.debug("DELETE deleteDeliveryManager deliveryManagerId received {} ", deliveryManagerId);
        var deliveryManager = managerRepository.findById(deliveryManagerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DeliveryManager Not Found"));
        managerRepository.deleteManager(deliveryManagerId);
        log.debug("DELETE deleteDeliveryManager deliveryManagerId deleted {} ", deliveryManagerId);
        log.info("DeliveryManager deleted successfully deliveryManagerId {} ", deliveryManagerId);
    }

    @Override
    public Optional<DeliveryManager> findById(UUID deliveryManagerId) {
        return managerRepository.findById(deliveryManagerId);
    }

    @Override
    public Optional<DeliveryManager> findByPhoneNumber(String phoneNumber) {
        return managerRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<DeliveryManager> findAll(UUID storageId, String fullName) {
        var storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Not Found"));
        return managerRepository.findAll(storage.getStorageId(), fullName);
    }

    @Override
    public List<DeliveryManager> findAllByStorage(UUID storageId) {
        var storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Not Found"));
        return managerRepository.findAllByStorage(storage.getStorageId());
    }
}

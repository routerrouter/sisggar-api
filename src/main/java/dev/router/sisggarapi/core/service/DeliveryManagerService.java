package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.DeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerService {
    DeliveryManager createDeliveryManager(DeliveryManager deliveryManager);
    DeliveryManager updateDeliveryManager(DeliveryManager deliveryManager, UUID deliveryId);
    void delete(UUID deliveryManagerId);
    Optional<DeliveryManager> findById(UUID deliveryManagerId);
    Optional<DeliveryManager> findByPhoneNumber(String phoneNumber);
    List<DeliveryManager> findAll(UUID storageId, String fullName);
    List<DeliveryManager> findAllByStorage(UUID storageId);
}

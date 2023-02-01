package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.adapter.request.supplierMovement.MovementFindRequest;
import dev.router.sisggarapi.core.domain.SupplierMovement;

import java.util.List;
import java.util.UUID;

public interface SupplierMovementService {
    SupplierMovement createSupplierMovement(UUID storageId, SupplierMovement movement,UUID userId);
    List<SupplierMovement> findAll(UUID storageId, MovementFindRequest findRequest);
    void deleteSupplierMovement(UUID movementId);
}

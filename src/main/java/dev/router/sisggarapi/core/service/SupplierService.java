package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierService {
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Supplier supplier, UUID supplierId);
    void updateStatus(Supplier supplier, UUID supplierId);
    Optional<Supplier> findById(UUID supplierId);
    List<Supplier> findAll(String name, String nif, String telephone);
}

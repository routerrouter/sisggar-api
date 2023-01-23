package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Storage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageService {
    Storage createStorage(Storage storage);
    Storage updateStorage(Storage storage, UUID storageId);
    void delete(UUID storageId);
    Optional<Storage> findById(UUID storageId);
    Optional<Storage> findByDescription(String description);
    //Page<Storage> findAll(Specification<Storage> spec, Pageable pageable);
    List<Storage> findAll(String description);
}

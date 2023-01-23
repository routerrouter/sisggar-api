package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.core.domain.Existence;

import java.util.List;
import java.util.UUID;

public interface ExistenceService {
    Existence saveExistence(ExistenceRequest request);
    List<Existence> findAll(UUID storageId);
    void transferBetweenStorage(UUID storageOrigin, ExistenceRequest request);
}

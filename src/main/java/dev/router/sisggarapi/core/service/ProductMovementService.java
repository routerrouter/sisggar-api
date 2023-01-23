package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.adapter.dto.ProductMovementModelDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductMovementService {
    ProductMovementModelDto createProductMovement(ProductMovementModelDto modelDto);
    List<ProductMovementModelDto> findAll(UUID storageId,
                                          UUID providerId,
                                          LocalDate initialDate,
                                          LocalDate finalDate,
                                          String movementType,
                                          String productType);
}

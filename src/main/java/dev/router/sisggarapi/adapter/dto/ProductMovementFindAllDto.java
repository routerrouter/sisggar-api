package dev.router.sisggarapi.adapter.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductMovementFindAllDto {
    private UUID storageId;
    private UUID providerId;
    private LocalDate initialDate;
    private LocalDate finalDate;
    private String movementType;
    private String productType;
    Pageable pageable;
}

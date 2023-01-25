package dev.router.sisggarapi.adapter.response.productMovement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.request.supplierMovement.SupplierMovementRequest;
import dev.router.sisggarapi.adapter.response.storage.StorageResponse;
import dev.router.sisggarapi.adapter.response.supplier.SupplierResponse;
import dev.router.sisggarapi.core.domain.Storage;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierMovementResponse {
    private UUID supplierMovementId;
    private StorageResponse storage;
    private SupplierResponse supplier;
    private String movementType;
    private String productType;
    private Long quantity;
    private String documentNumber;
    private String movementNote;
    private LocalDate movementDate;
    private Month movementMonth;

}

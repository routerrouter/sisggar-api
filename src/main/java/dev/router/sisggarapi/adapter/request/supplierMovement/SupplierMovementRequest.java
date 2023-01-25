package dev.router.sisggarapi.adapter.request.supplierMovement;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Getter
@Setter
public class SupplierMovementRequest {

    public interface SupplierMovementView {
        public static interface RegistrationPost {}
        public static interface SupplierMovimentPut {}
    }

    @JsonView(SupplierMovementView.SupplierMovimentPut.class)
    private UUID supplierMovementId;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private Long quantity;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private String documentNumber;

    @JsonView({SupplierMovementView.SupplierMovimentPut.class,
            SupplierMovementView.RegistrationPost.class})
    private String movementNote;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private ProductType productType;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private MovementType movementType;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private LocalDate movementDate;

    @JsonView(SupplierMovementView.SupplierMovimentPut.class)
    private Month movementMonth;

    @JsonView(SupplierMovementView.RegistrationPost.class)
    private UUID supplierId;
}

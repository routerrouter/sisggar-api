package dev.router.sisggarapi.adapter.request.supplierMovement;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MovementFindRequest {

    private UUID supplierId;

    @NotBlank
    private LocalDate initialDate;
    @NotBlank
    private LocalDate finalDate;

    private String movementType;

    private String productType;
}

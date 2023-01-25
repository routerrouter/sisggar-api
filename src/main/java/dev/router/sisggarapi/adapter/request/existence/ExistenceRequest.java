package dev.router.sisggarapi.adapter.request.existence;

import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExistenceRequest {

    @NotNull
    private Long quantity;
    @NotNull
    private ProductType productType;
    @NotNull
    private UUID storageId;
    @NotNull
    private UUID userId;
    @NotNull
    private MovementType movementType;


}

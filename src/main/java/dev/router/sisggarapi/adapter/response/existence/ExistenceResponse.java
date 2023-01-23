package dev.router.sisggarapi.adapter.response.existence;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExistenceResponse {

    private UUID existenceId;
    private ProductType productType;
    private Long quantity;

}

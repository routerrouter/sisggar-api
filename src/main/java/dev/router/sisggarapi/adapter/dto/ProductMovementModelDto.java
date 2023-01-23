package dev.router.sisggarapi.adapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMovementModelDto {

    public interface ProductMovementView {
        public static interface RegistrationPost {}
        public static interface ProdutcMovimentPut {}
    }

    @JsonView(ProductMovementView.ProdutcMovimentPut.class)
    private UUID productMovementId;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private Long quantity;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private String documentNumber;

    @JsonView({ProductMovementView.ProdutcMovimentPut.class,
            ProductMovementView.RegistrationPost.class})
    private String movementNote;


    @JsonView(ProductMovementView.RegistrationPost.class)
    private ProductType productType;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private MovementType movementType;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private UUID providerModelId;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private UUID userModelId;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private UUID storageId;

    @JsonView(ProductMovementView.RegistrationPost.class)
    private LocalDate movementDate;

    @JsonView(ProductMovementView.ProdutcMovimentPut.class)
    private Month movementMonth;
}

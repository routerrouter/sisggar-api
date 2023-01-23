package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.adapter.dto.ProductMovementModelDto;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_PRODUCTMOVEMENT")
public class ProductMovement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productMovementId;

    @Column(nullable = false)
    private Long quantity;

    @Column
    private String documentNumber;

    @Column
    private String movementNote;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Month movementMonth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="provider_id")
    private Provider provider;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="storage_id")
    private Storage storage;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate movementDate;


    public ProductMovement(ProductMovementModelDto modelDto) {
        var storage = new Storage();
        var user = new User();
        var provider = new Provider();
        storage.setStorageId(modelDto.getStorageId());
        this.storage = storage;
        user.setUserId(modelDto.getUserModelId());
        this.user = user;
        provider.setProviderId(modelDto.getProviderModelId());
        this.provider = provider;
        this.creationDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}

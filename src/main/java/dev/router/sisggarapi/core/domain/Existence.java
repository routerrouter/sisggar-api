package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_EXISTENCE")
public class Existence implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID existenceId;

    @Column
    private Long quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="storage_id")
    private Storage storage;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Existence(ExistenceRequest request) {
        var storage = new Storage();
        storage.setStorageId(request.getStorageId());
        var user = new User();
        user.setUserId(request.getUserId());
        this.storage = storage;
        this.user = user;
        this.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        this.quantity = request.getQuantity();
        this.productType = request.getProductType();
    }

    public Long updateQuantity(MovementType movementType, Long newQuantity){
        if ( movementType.equals(movementType.INPUT) )
            this.setQuantity(this.quantity + newQuantity);
        else
            this.setQuantity(this.quantity - newQuantity);

        return this.getQuantity();
    }
}


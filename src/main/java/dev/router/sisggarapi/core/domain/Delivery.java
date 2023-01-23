package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.core.domain.enums.DeliveryStatus;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_DELIVERY")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deliveryId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status deliveyStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="location_id")
    private Location deliveryLocation;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="costumer_id")
    private Costumer costumer;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_manager_id")
    private DeliveryManager deliveryManager;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User employeeRegister;

    @Column
    private Long receivedQuantity;

    @Column
    private Long deliveredQuantity;

    @Column
    private String responsiblePerson;

    @Column
    private String deliveryNote;
}

package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_DELIVERYMANAGER")
public class DeliveryManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deliveryManagerId;
    @Column(nullable = false, unique = true)
    private String fullName;
    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="storage_id")
    private Storage storage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TB_COSTUMER_DELIVERY_MANAGERS",
            joinColumns = @JoinColumn(name = "delivery_manager_id"),
            inverseJoinColumns = @JoinColumn(name = "costumer_id"))
    Set<Costumer> costumers;


}

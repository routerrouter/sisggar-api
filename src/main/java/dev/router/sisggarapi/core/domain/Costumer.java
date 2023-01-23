package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COSTUMER")
public class Costumer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID costumerId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 10, max = 16)
    private String nif;

    @Column()
    private String address;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(unique = true)
    private String email;

    private Integer deliveryLimit;

    private Integer possession;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status costumerStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "costumers",fetch = FetchType.LAZY)
    Set<Location> locations;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "costumers",fetch = FetchType.LAZY)
    Set<DeliveryManager> deliveryManagers;





}

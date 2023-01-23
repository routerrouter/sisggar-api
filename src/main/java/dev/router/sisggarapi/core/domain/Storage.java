package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_STORAGE")
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID storageId;

    @Column(nullable = false, unique = true)
    private String description;

    @Column(nullable = false)
    private Integer storageLimit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status storageStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy="storage", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Location> locations;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy="storage", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<DeliveryManager> managers;
}

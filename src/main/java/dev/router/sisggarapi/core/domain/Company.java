package dev.router.sisggarapi.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COMPANY")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 10, max = 16)
    private String nif;

    @Column()
    private String address;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column
    private String imageUrl;




}

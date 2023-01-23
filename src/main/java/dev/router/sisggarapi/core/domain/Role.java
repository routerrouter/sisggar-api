package dev.router.sisggarapi.core.domain;

import dev.router.sisggarapi.core.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ROLES")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleType roleName;

    /*@Override
    @JsonIgnore
    public String getAuthority() {
        return this.roleName.toString();
    }*/
}

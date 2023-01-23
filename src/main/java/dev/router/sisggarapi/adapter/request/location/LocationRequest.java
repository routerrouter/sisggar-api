package dev.router.sisggarapi.adapter.request.location;

import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {

    @NotBlank(message = "Descrição é obrigatória")
    private String description;
    @NotNull
    private UUID storageId;

}

package dev.router.sisggarapi.adapter.request.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageRequest {

    @NotBlank(message = "Descrição é obrigatório")
    private String description;
    private Integer storageLimit;

}

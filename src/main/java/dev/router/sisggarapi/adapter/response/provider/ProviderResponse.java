package dev.router.sisggarapi.adapter.response.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.core.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderResponse {

    private UUID providerId;
    private String name;
    private String nif;
    private String telephone;
    private String address;
    private String email;
    private Status providerStatus;
}

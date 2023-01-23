package dev.router.sisggarapi.adapter.request.provider;

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
public class ProviderRequest {

    public interface ProviderView {
        public static interface RegistrationPost {}
        public static interface ProviderPut {}
        public static interface UpdateStatus {}
    }

    @NotBlank(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @JsonView({ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    private String name;

    @NotBlank(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @JsonView({ProviderView.RegistrationPost.class,  ProviderView.ProviderPut.class})
    @Size(min = 10, max = 16, groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    private String nif;

    @NotBlank(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @JsonView({ProviderView.RegistrationPost.class,  ProviderView.ProviderPut.class})
    private String telephone;

    @NotBlank(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @JsonView({ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    private String address;

    @NotBlank(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @JsonView({ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    @Email(groups = {ProviderView.RegistrationPost.class, ProviderView.ProviderPut.class})
    private String email;

    @NotNull(groups = {ProviderView.UpdateStatus.class})
    @JsonView({ProviderView.UpdateStatus.class})
    private Status providerStatus;
}

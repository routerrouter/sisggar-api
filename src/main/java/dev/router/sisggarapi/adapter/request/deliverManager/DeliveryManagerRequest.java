package dev.router.sisggarapi.adapter.request.deliverManager;

import com.fasterxml.jackson.annotation.JsonView;
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
public class DeliveryManagerRequest {

    public interface ManagerView {
        public static interface RegistrationPost {}
        public static interface ManagerPut{}
    }

    @NotBlank(message = "Nome completo é obrigatório",groups = {DeliveryManagerRequest.ManagerView.RegistrationPost.class, DeliveryManagerRequest.ManagerView.ManagerPut.class})
    @JsonView({DeliveryManagerRequest.ManagerView.RegistrationPost.class, DeliveryManagerRequest.ManagerView.ManagerPut.class})
    private String fullName;

    @NotBlank(message = "Telefone é obrigatório",groups = {DeliveryManagerRequest.ManagerView.RegistrationPost.class, DeliveryManagerRequest.ManagerView.ManagerPut.class})
    @JsonView({DeliveryManagerRequest.ManagerView.RegistrationPost.class, DeliveryManagerRequest.ManagerView.ManagerPut.class})
    private String phoneNumber;

    @NotNull(message = "Telefone é obrigatório",groups = {DeliveryManagerRequest.ManagerView.RegistrationPost.class})
    @JsonView({DeliveryManagerRequest.ManagerView.RegistrationPost.class})
    private UUID storageId;
}

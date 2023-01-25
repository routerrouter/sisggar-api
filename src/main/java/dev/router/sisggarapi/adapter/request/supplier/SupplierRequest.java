package dev.router.sisggarapi.adapter.request.supplier;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SupplierRequest {

    public interface SupplierView {
        public static interface RegistrationPost {}
        public static interface SupplierPut {}
        public static interface UpdateStatus {}
    }

    @NotBlank(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @JsonView({SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    private String name;

    @NotBlank(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @JsonView({SupplierView.RegistrationPost.class,  SupplierView.SupplierPut.class})
    @Size(min = 10, max = 16, groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    private String nif;

    @NotBlank(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @JsonView({SupplierView.RegistrationPost.class,  SupplierView.SupplierPut.class})
    private String telephone;

    @NotBlank(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @JsonView({SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    private String address;

    @NotBlank(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @JsonView({SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    @Email(groups = {SupplierView.RegistrationPost.class, SupplierView.SupplierPut.class})
    private String email;

    @NotNull(groups = {SupplierView.UpdateStatus.class})
    @JsonView({SupplierView.UpdateStatus.class})
    private Status SupplierStatus;
}

package dev.router.sisggarapi.adapter.request.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyImageRequest {
    @NotBlank(message = "Url da imagem é obrigatório")
    private String imageUrl;
}

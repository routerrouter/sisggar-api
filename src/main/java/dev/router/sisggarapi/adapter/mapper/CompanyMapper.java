package dev.router.sisggarapi.adapter.mapper;

import dev.router.sisggarapi.adapter.request.company.CompanyImageRequest;
import dev.router.sisggarapi.adapter.request.company.CompanyRequest;
import dev.router.sisggarapi.adapter.response.company.CompanyResponse;
import dev.router.sisggarapi.core.domain.Company;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    private final ModelMapper mapper;

    public Company toCompany(CompanyRequest request){
        return mapper.map(request, Company.class);
    }

    public Company toCompany(CompanyImageRequest imageRequest){
        return mapper.map(imageRequest, Company.class);
    }

    public CompanyResponse toCompanyResponse(Company company) {
        return mapper.map(company, CompanyResponse.class);
    }

    public List<CompanyResponse> toCompanyResponseList(List<Company> companies) {
        return companies.stream()
                .map(this::toCompanyResponse)
                .collect(Collectors.toList());
    }
}

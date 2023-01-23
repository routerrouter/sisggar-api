package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Company;
import dev.router.sisggarapi.core.repository.CompanyRepository;
import dev.router.sisggarapi.core.service.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Company updateOrSaveCompanyInfo(Company company) {

        if (!companyRepository.existCompany()) {
            company.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            company.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        } else {
            if (company.getCompanyId() != null) {
                company = companyRepository.findById(company.getCompanyId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
            } else {
                company = companyRepository.findCompany();
                company.setCompanyId(company.getCompanyId());
            }

        }
        company = companyRepository.save(company);
        return company;
    }


    @Override
    public String updateUrlImage(Company company, UUID companyId) {
        var optEmpresa = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        optEmpresa.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        optEmpresa.setImageUrl(company.getImageUrl());
        optEmpresa = companyRepository.save(optEmpresa);

        return optEmpresa.getImageUrl();
    }

    @Override
    public Company getCompanyDetails() {
        return companyRepository.findCompany();
    }
}

package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Company;

import java.util.UUID;

public interface CompanyService {
    Company updateOrSaveCompanyInfo(Company request);
    String updateUrlImage(Company request, UUID companyId);
    Company getCompanyDetails();
}

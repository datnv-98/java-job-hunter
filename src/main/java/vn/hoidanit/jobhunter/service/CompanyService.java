package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company) {
        if (company != null) {
            return companyRepository.save(company);
        }
        return null;
    }

    public ResultPaginationDTO getAllCompany(Specification<Company> spec, Pageable pageable) {
        @SuppressWarnings("null")
        Page<Company> page = companyRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1); // Page number is zero-based
        meta.setPageSize(pageable.getPageSize()); // Number of items per page
        meta.setTotalPages(page.getTotalPages()); // Total number of items
        meta.setTotalElements(page.getTotalElements()); // Total number of pages
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(page.getContent());
        return resultPaginationDTO;
    }

    @SuppressWarnings("null")
    public void deleteCompany(Long id) {
        Optional<Company> exisCompany = companyRepository.findById(id);
        if (exisCompany.isPresent()) {
            companyRepository.deleteById(id);
        }
    }

    public Company updateCompany(Long id, Company companyUpdate) {
        Company exisCompany = companyRepository.findById(id).orElse(null);
        if (exisCompany != null) {
            exisCompany.setName(companyUpdate.getName());
            exisCompany.setDescription(companyUpdate.getDescription());
            exisCompany.setAddress(companyUpdate.getAddress());
            exisCompany.setLogo(companyUpdate.getLogo());
            return companyRepository.save(exisCompany);
        }
        return null;

    }
}

package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company saveCompany(Company company) {
        if (company != null) {
            return companyRepository.save(company);
        }
        return null;
    }

    public ResPaginationDTO getAllCompany(Specification<Company> spec, Pageable pageable) {
        @SuppressWarnings("null")
        Page<Company> page = companyRepository.findAll(spec, pageable);
        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
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
            Company com = exisCompany.get();
            // fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }

    public Company updateCompany(Long id, Company companyUpdate) {
        Company existCompany = companyRepository.findById(id).orElse(null);
        if (existCompany != null) {
            existCompany.setName(companyUpdate.getName());
            existCompany.setDescription(companyUpdate.getDescription());
            existCompany.setAddress(companyUpdate.getAddress());
            existCompany.setLogo(companyUpdate.getLogo());
            return companyRepository.save(existCompany);
        }
        return null;

    }

    public Boolean isExistCompany(Long id) {
        Optional<Company> exisCompany = companyRepository.findById(id);
        return exisCompany.isPresent();
    }

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }
}

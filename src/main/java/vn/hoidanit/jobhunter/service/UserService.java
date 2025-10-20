package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public ResPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        @SuppressWarnings("null")
        Page<User> page = userRepository.findAll(spec, pageable);
        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1); // Page number is zero-based
        meta.setPageSize(pageable.getPageSize()); // Number of items per page
        meta.setTotalPages(page.getTotalPages()); // Total number of items
        meta.setTotalElements(page.getTotalElements()); // Total number of pages
        resultPaginationDTO.setMeta(meta);

        // remove sensitive data
        List<ResUserDTO> listUsers = page.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        new ResUserDTO.Company(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(listUsers);
        return resultPaginationDTO;
    }

    public User createUser(User user) {
        // Logic to save user to the database
        if (user == null) {
            return null;
        }
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // Logic to delete user from the database
        if (id.longValue() > 0) {
            this.userRepository.deleteById(id);
        }
    }

    @SuppressWarnings("null")
    public User getDetailUser(Long id) {
        // Logic to get user detail from the database
        return this.userRepository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public User updateUser(Long userId, User userUpdate) {
        User existingUser = this.userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setName(userUpdate.getName());
            existingUser.setGender(userUpdate.getGender());
            existingUser.setAge(userUpdate.getAge());
            existingUser.setAddress(userUpdate.getAddress());
            existingUser.setCompany(userUpdate.getCompany());
            return this.userRepository.save(existingUser);
        }
        return null;
    }

    public User getUserByUserName(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @SuppressWarnings("null")
    public ResCreateUserDTO convertToResCreateUserDTO(User createdUser) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.Company company = new ResCreateUserDTO.Company();
        // lay thong tin company tu db
        Optional<Company> companyFromDb = this.companyRepository.findById(createdUser.getCompany().getId());
        res.setId(createdUser.getId());
        res.setName(createdUser.getName());
        res.setEmail(createdUser.getEmail());
        res.setAge(createdUser.getAge());
        res.setGender(createdUser.getGender());
        res.setAddress(createdUser.getAddress());
        res.setCreatedAt(createdUser.getCreatedAt());
        company.setId(companyFromDb.get().getId());
        company.setName(companyFromDb.get().getName());
        res.setCompany(company);
        return res;
    }

    public User fetchUserById(Long id) {
        @SuppressWarnings("null")
        Optional<User> existUser = this.userRepository.findById(id);
        return existUser.get();
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.Company company = new ResUserDTO.Company();
        // lay thong tin company tu db
        @SuppressWarnings("null")
        Optional<Company> companyFromDb = this.companyRepository.findById(user.getId());
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        company.setId(companyFromDb.get().getId());
        company.setName(companyFromDb.get().getName());
        res.setCompany(company);
        return res;
    }

    @SuppressWarnings("null")
    public ResUpdateUserDTO convertToResUpdateUserDTO(User usUpdate) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.Company company = new ResUpdateUserDTO.Company();
        // lay thong tin company tu db
        Optional<Company> companyFromDb = this.companyRepository.findById(usUpdate.getCompany().getId());
        res.setId(usUpdate.getId());
        res.setName(usUpdate.getName());
        res.setEmail(usUpdate.getEmail());
        res.setAge(usUpdate.getAge());
        res.setGender(usUpdate.getGender());
        res.setAddress(usUpdate.getAddress());
        res.setCreatedAt(usUpdate.getCreatedAt());
        company.setId(companyFromDb.get().getId());
        company.setName(companyFromDb.get().getName());
        res.setCompany(company);
        return res;

    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.getUserByUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}

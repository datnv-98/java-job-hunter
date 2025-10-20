package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO create(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        // check company
        if (job.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(job.getCompany().getId());
            if (cOptional.isPresent()) {
                job.setCompany(cOptional.get());
            }
        }
        ;
        // create a job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public Optional<Job> fetchJobById(Long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional;
        }
        return null;
    }

    public ResUpdateJobDTO update(Job job, Job jobInDB) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            jobInDB.setSkills(dbSkills);
        }

        // check company
        if (job.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(job.getCompany().getId());
            if (cOptional.isPresent()) {
                jobInDB.setCompany(cOptional.get());
            }
        }
        ;

        // update correct info
        jobInDB.setName(job.getName());
        jobInDB.setSalary(job.getSalary());
        jobInDB.setQuantity(job.getQuantity());
        jobInDB.setLocation(job.getLocation());
        jobInDB.setLevel(job.getLevel());
        jobInDB.setStartDate(job.getStartDate());
        jobInDB.setEndDate(job.getEndDate());
        jobInDB.setCreatedAt(job.getCreatedAt());
        jobInDB.setCreatedBy(job.getCreatedBy());
        jobInDB.setActive(job.isActive());

        // update a job
        Job currentJob = this.jobRepository.save(jobInDB);

        // convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResPaginationDTO fetAllSkills(Specification<Job> spec, Pageable pageable) {
        @SuppressWarnings("null")
        Page<Job> page = jobRepository.findAll(spec, pageable);
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

}

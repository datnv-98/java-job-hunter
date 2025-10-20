package vn.hoidanit.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;
    private String name;
    private double salary;
    private int quantity;
    private String location;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;

    private List<String> skills;

    private Instant createdAt;
    private String createdBy;
}

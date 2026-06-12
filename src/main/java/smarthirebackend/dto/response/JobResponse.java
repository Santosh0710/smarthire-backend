package smarthirebackend.dto.response;

import smarthirebackend.model.JobStatus;
import smarthirebackend.model.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class JobResponse
{
    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private String salaryRange;
    private JobType jobType;
    private JobStatus status;

    // We show employer's name, not the entire User object
    // This is why DTOs are important!
    private String postedByName;
    private String postedByEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

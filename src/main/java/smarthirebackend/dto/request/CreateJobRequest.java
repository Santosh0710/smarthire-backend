package smarthirebackend.dto.request;

import smarthirebackend.model.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateJobRequest
{
    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    // Optional - not all jobs show salary
    private String salaryRange;

    @NotNull(message = "Job type is required")
    private JobType jobType;
}

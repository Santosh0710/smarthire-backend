package smarthirebackend.service.impl;

import jakarta.transaction.Transactional;
import smarthirebackend.dto.request.CreateJobRequest;
import smarthirebackend.dto.response.JobResponse;
import smarthirebackend.exception.UnauthorizedActionException;
import smarthirebackend.model.Job;
import smarthirebackend.model.JobStatus;
import smarthirebackend.model.Role;
import smarthirebackend.model.User;
import smarthirebackend.repository.JobRepository;
import smarthirebackend.repository.UserRepository;
import smarthirebackend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public JobResponse createJob(CreateJobRequest request,
                                 String employerEmail) {

        // Step 1: Find the employer by email
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));

        // Step 2: Make sure they are actually an EMPLOYER
        // A JOB_SEEKER should not be able to post jobs
        if (employer.getRole() != Role.EMPLOYER) {
            throw new UnauthorizedActionException(
                    "Only employers can post jobs");
        }

        // Step 3: Build and save the job
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .company(request.getCompany())
                .location(request.getLocation())
                .salaryRange(request.getSalaryRange())
                .jobType(request.getJobType())
                // New jobs are always OPEN by default
                .status(JobStatus.OPEN)
                .postedBy(employer)
                .build();

        Job savedJob = jobRepository.save(job);

        // Step 4: Convert to response DTO and return
        return mapToJobResponse(savedJob);
    }

    @Override
    @Transactional
    public List<JobResponse> getAllOpenJobs() {
        // Only return OPEN jobs to job seekers
        return jobRepository.findByStatus(JobStatus.OPEN)
                .stream()
                .map(this::mapToJobResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + id));
        return mapToJobResponse(job);
    }

    @Override
    @Transactional
    public JobResponse updateJob(Long id, CreateJobRequest request,
                                 String employerEmail) {

        // Step 1: Find the job
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + id));

        // Step 2: Make sure the person updating is the one who posted it
        // You cannot edit someone else's job posting
        if (!job.getPostedBy().getEmail().equals(employerEmail)) {
            throw new UnauthorizedActionException(
                    "You can only update your own job postings");
        }

        // Step 3: Update the fields
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalaryRange(request.getSalaryRange());
        job.setJobType(request.getJobType());

        Job updatedJob = jobRepository.save(job);
        return mapToJobResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long id, String employerEmail) {

        // Step 1: Find the job
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Job not found with id: " + id));

        // Step 2: Make sure the person deleting is the one who posted it
        if (!job.getPostedBy().getEmail().equals(employerEmail)) {
            throw new UnauthorizedActionException(
                    "You can only delete your own job postings");
        }

        jobRepository.delete(job);
    }

    // ─── PRIVATE HELPER ──────────────────────────────────────

    // Converts Job entity to JobResponse DTO
    // We use this in every method to avoid code repetition
    private JobResponse mapToJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .location(job.getLocation())
                .salaryRange(job.getSalaryRange())
                .jobType(job.getJobType())
                .status(job.getStatus())
                // Show employer name instead of full User object
                .postedByName(job.getPostedBy().getFirstName()
                        + " " + job.getPostedBy().getLastName())
                .postedByEmail(job.getPostedBy().getEmail())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
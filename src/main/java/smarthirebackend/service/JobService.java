package smarthirebackend.service;

import smarthirebackend.dto.request.CreateJobRequest;
import smarthirebackend.dto.response.JobResponse;

import java.util.List;

public interface JobService
{
//    Employer Posts a new Job
    JobResponse createJob(CreateJobRequest request , String employerEmail);

//    Anyone can browse all open jobs
    List<JobResponse> getAllOpenJobs();

//    Get Single job by Id
    JobResponse getJobById(Long id);

    // Employer updates their job
    JobResponse updateJob(Long id, CreateJobRequest request,
                          String employerEmail);

    // Employer deletes their job
    void deleteJob(Long id, String employerEmail);
}

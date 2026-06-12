package smarthirebackend.controller;

import smarthirebackend.dto.request.CreateJobRequest;
import smarthirebackend.dto.response.JobResponse;
import smarthirebackend.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // POST /api/jobs — Create a new job (Employer only)
    // Principal gives us the currently logged-in user's email
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            Principal principal) {

        // principal.getName() returns the email from JWT token
        JobResponse response = jobService.createJob(
                request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/jobs — Get all open jobs (Public)
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllOpenJobs() {
        return ResponseEntity.ok(jobService.getAllOpenJobs());
    }

    // GET /api/jobs/{id} — Get single job (Public)
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // PUT /api/jobs/{id} — Update job (Employer only)
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody CreateJobRequest request,
            Principal principal) {

        JobResponse response = jobService.updateJob(
                id, request, principal.getName());
        return ResponseEntity.ok(response);
    }

    // DELETE /api/jobs/{id} — Delete job (Employer only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            Principal principal) {

        jobService.deleteJob(id, principal.getName());
        // 204 No Content = success with no response body
        return ResponseEntity.noContent().build();
    }
}
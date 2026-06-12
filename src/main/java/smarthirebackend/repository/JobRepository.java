package smarthirebackend.repository;

import smarthirebackend.model.Job;
import smarthirebackend.model.JobStatus;
import smarthirebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job , Long>
{
    // Find all jobs with a specific status
    // SQL: SELECT * FROM jobs WHERE status = ?
    List<Job> findByStatus(JobStatus status);

    // Find all jobs posted by a specific employer
    // SQL: SELECT * FROM jobs WHERE posted_by = ?
    List<Job> findByPostedBy(User postedBy);

    // Find jobs by location
    // SQL: SELECT * FROM jobs WHERE location = ?
    List<Job> findByLocation(String location);

    // Find jobs containing keyword in title
    // SQL: SELECT * FROM jobs WHERE title LIKE %keyword%
    List<Job> findByTitleContainingIgnoreCase(String keyword);
}

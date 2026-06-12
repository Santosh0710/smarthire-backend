package smarthirebackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Job
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Job Title - e.g "Senior Java Developer"
    @Column(nullable = false)
    private String title;

//    Full Job Description- TEXT allows more than 255 chars
    @Column(nullable = false , columnDefinition = "TEXT")
    private String description;

//    Company Offering the job
    @Column(nullable = false)
    private String company;

//    Where the job is located
    @Column(nullable = false)
    private String location;

//    Optional salary range
    private String salaryRange;

//    FULL time , part time , contrwct
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    // Open or Closed
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    // Which employer posted this job
    // ManyToOne = many jobs can belong to one user
    // LAZY = don't load user data unless we specifically ask for it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

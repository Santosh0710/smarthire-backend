package smarthirebackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// Tells Hibernate this class = a database table
@Entity

// Table name is users
@Table(name = "users")

// Lombok generates all getters and setters
@Data

// Lombok generates empty constructor
@NoArgsConstructor

// LOmbok generates constructor with all fields
@AllArgsConstructor

// Lonbok Builder Pattern
@Builder

public class User
{
//    Primary Key - auto incremented by users
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Maps to column "first_name" cannot be null
    @Column(name = "first_name" , nullable = false)
    private String first_name;

    // Maps to column "last_name", cannot be null
    @Column(name = "last_name", nullable = false)
    private String lastName;

//    Email must be unique and cannot be null
    @Column(unique = true , nullable = false)
    private String email;

//    Password cannot br null and will be stored encrypted
    @Column(nullable = false)
    private String password;

    // Store enum as string ("JOB_SEEKER") not number (0)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Automatically set when record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically updated every time record changes
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

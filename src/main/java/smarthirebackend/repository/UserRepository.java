package smarthirebackend.repository;

import smarthirebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository marks this as data access component
// JpaRepository<User , Long> means :
// - we are working with user entity
// the primary key type is long

@Repository
public interface UserRepository extends JpaRepository<User , Long>
{
    // Spring generates this SQL automatically:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring generates this SQL automatically:
    // SELECT COUNT(*) > 0 FROM users WHERE email = ?

    boolean existsByEmail(String email);
}

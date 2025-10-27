package psu.edu.GameFlix.Repoitories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmailContainingIgnoreCase(String email);
    List<User> findByDisplayNameContainingIgnoreCase(String displayName);
    
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

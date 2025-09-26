package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Models.UserSummary;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new psu.edu.GameFlix.Models.UserSummary(u.id, u.email, u.displayName, u.isEmailVerified, u.isAdmin, CAST(u.createdAt AS string)) FROM User u")
    List<UserSummary> findAllUserSummaries();
}

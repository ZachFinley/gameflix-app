package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.UserRole;
import psu.edu.GameFlix.Models.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserId(int userId);

    List<UserRole> findByRoleId(int roleId);
}

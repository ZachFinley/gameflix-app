package psu.edu.GameFlix.Repoitories;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

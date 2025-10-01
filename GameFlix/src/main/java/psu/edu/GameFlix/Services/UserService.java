package psu.edu.GameFlix.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Repoitories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }
    public List<User> findAll() {
		return userRepository.findAll();
	}
}

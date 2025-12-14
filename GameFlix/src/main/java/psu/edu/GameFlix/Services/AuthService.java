// psu/edu/GameFlix/services/AuthService.java
package psu.edu.GameFlix.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Repoitories.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public String register(Map<String, String> req) {
        String email = req.get("username").trim().toLowerCase();
        if (email.isEmpty() || req.get("password").trim().isEmpty()) {
            return "Email and password are required";
        }
        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        User u = new User();
        u.setEmail(email);
        u.setDisplayName(email);
        u.setPasswordHash(encoder.encode(req.get("password")));

        userRepository.save(u);
        return "User registered successfully";
    }

    @Transactional(readOnly = true)
    public String login(Map<String, String> req) {
        String email = req.get("username").trim().toLowerCase();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Invalid email or password";
        }
        User u = userOpt.get();
        boolean matches = encoder.matches(req.get("password"), u.getPasswordHash());
        return matches ? "Login successful" : "Invalid email or password";
    }
}

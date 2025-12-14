package psu.edu.GameFlix.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psu.edu.GameFlix.Services.AuthService;

import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Map<String, String> req) {
        String result = authService.register(req);
        if ("Email already exists".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        if ("Email and password are required".equals(result) ) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody Map<String, String> req) {
       String result = authService.login(req);
        if ("Login successful".equals(result)) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}

package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.Library;
import java.util.Map;
import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Models.UserRole;
import psu.edu.GameFlix.Models.Wishlist;
import psu.edu.GameFlix.Services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/search/email")
    public List<User> searchUsersByEmail(@RequestParam String email) {
        return userService.searchUsersByEmail(email);
    }

    @GetMapping("/search/name")
    public List<User> searchUsersByDisplayName(@RequestParam String name) {
        return userService.searchUsersByDisplayName(name);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @GetMapping("/{userId}/roles")
    public List<UserRole> getUserRoles(@PathVariable int userId) {
        return userService.findUserRoles(userId);
    }

    @GetMapping("/{userId}/library")
    public List<Library> getUserLibrary(@PathVariable int userId) {
        return userService.findUserLibrary(userId);
    }

    @PostMapping("/{userId}/library")
    public Library addToLibrary(@PathVariable int userId, @RequestBody Map<String, Integer> req) {
        int gameId = req.getOrDefault("gameId", -1);
        Library lib = new Library();
        lib.setUserId(userId);
        lib.setGameId(gameId);
        return userService.addToLibrary(lib);
    }

    @DeleteMapping("/{userId}/library/{gameId}")
    public void removeFromLibrary(@PathVariable int userId, @PathVariable int gameId) {
        userService.removeFromLibrary(userId, gameId);
    }

    @GetMapping("/{userId}/wishlist")
    public List<Wishlist> getUserWishlist(@PathVariable int userId) {
        return userService.findUserWishlist(userId);
    }

    @GetMapping("/{userId}/is-admin")
    public boolean isUserAdmin(@PathVariable int userId) {
        return userService.isUserAdmin(userId);
    }
}
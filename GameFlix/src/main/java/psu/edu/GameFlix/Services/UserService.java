package psu.edu.GameFlix.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import psu.edu.GameFlix.Models.Library;
import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Models.UserRole;
import psu.edu.GameFlix.Models.Wishlist;
import psu.edu.GameFlix.Repoitories.LibraryRepository;
import psu.edu.GameFlix.Repoitories.UserRepository;
import psu.edu.GameFlix.Repoitories.UserRoleRepository;
import psu.edu.GameFlix.Repoitories.WishlistRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final LibraryRepository libraryRepository;
    private final WishlistRepository wishlistRepository;

    public UserService(UserRepository theUserRepository, UserRoleRepository theUserRoleRepository, LibraryRepository theLibraryRepository, WishlistRepository theWishlistRepository) {
        userRepository = theUserRepository;
        userRoleRepository = theUserRoleRepository;
        libraryRepository = theLibraryRepository;
        wishlistRepository = theWishlistRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email.trim());
    }

    public List<User> searchUsersByDisplayName(String displayName) {
        return userRepository.findByDisplayNameContainingIgnoreCase(displayName.trim());
    }

    public Optional<User> findUserById(int userId) {
        return userRepository.findById(userId);
    }

    public List<UserRole> findUserRoles(int userId) {
        return userRoleRepository.findByUserId(userId);
    }

    public List<Library> findUserLibrary(int userId) {
        return libraryRepository.findByUserId(userId);
    }

    public List<Wishlist> findUserWishlist(int userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public boolean isUserAdmin(int userId) {
        Optional<User> user = findUserById(userId);
        return user.map(User::isAdmin).orElse(false);
    }
}

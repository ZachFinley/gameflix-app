package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.Wishlist;
import psu.edu.GameFlix.Models.WishlistId;

public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {
    List<Wishlist> findByUserId(int userId);

    List<Wishlist> findByGameId(int gameId);
}

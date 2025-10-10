package psu.edu.GameFlix.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlists")
@IdClass(WishlistId.class)
public class Wishlist {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "game_id")
    private int gameId;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}

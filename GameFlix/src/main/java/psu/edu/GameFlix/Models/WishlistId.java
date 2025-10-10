package psu.edu.GameFlix.Models;

import java.io.Serializable;
import java.util.Objects;

public class WishlistId implements Serializable {
    private int userId;
    private int gameId;

    public WishlistId() {
    }

    public WishlistId(int userId, int gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

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
}

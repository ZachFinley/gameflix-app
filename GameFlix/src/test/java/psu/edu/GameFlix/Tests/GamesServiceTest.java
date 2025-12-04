package psu.edu.GameFlix.Tests;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Services.GameService;
import psu.edu.GameFlix.Services.UserService;

@SpringBootTest
class GameServiceTest {
    @Autowired
    public GameService gameService;
    @Autowired
    public UserService userService;

    @Test
    void getAllGames_ShouldReturnList() {
        var games = gameService.findAllGames();
        Assertions.assertFalse(games.isEmpty());
    }
    
    @Test
    void findGameById_ShouldReturnAGame() {
        var game = gameService.findGameById(1);

        Assertions.assertFalse(game.isEmpty());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        var users = userService.findAllUsers();
        Assertions.assertFalse(users.isEmpty());
    }
    
}

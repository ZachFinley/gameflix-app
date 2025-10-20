package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Models.Review;
import psu.edu.GameFlix.Services.GameService;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/search")
    public List<Game> searchGames(@RequestParam String title) {
        return gameService.searchGamesByTitle(title);
    }

    @GetMapping("/{gameId}")
    public Game getGameById(@PathVariable int gameId) {
        return gameService.findGameById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
    }

    @GetMapping("/{gameId}/reviews")
    public List<Review> getGameReviews(@PathVariable int gameId) {
        return gameService.findGameReviews(gameId);
    }

    @GetMapping("/status/{status}")
    public List<Game> getGamesByStatus(@PathVariable String status) {
        return gameService.findGamesByStatus(status);
    }
}

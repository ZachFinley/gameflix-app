package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Models.Review;
import psu.edu.GameFlix.Services.GameService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/games/search")
    public List<Game> searchGames(@RequestParam String title) {
        return gameService.searchGamesByTitle(title);
    }

    @GetMapping("/games/{gameId}")
    public Game getGameById(@PathVariable int gameId) {
        return gameService.findGameById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
    }

    @GetMapping("/games/{gameId}/reviews")
    public List<Review> getGameReviews(@PathVariable int gameId) {
        return gameService.findGameReviews(gameId);
    }

    @GetMapping("/games/status/{status}")
    public List<Game> getGamesByStatus(@PathVariable String status) {
        return gameService.findGamesByStatus(status);
    }

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review) {
        return gameService.addReview(review);
    }

    @PutMapping("/reviews/{id}")
    public Review updateReview(@PathVariable int id, @RequestBody Review review) {
        return gameService.updateReview(id, review);
    }
}

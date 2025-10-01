package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.Game;
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
    public List<Game> findAllGames() {
        return gameService.findAll();
    }

    @GetMapping("/search")
    public List<Game> searchGames(@RequestParam String title) {
        return gameService.findByTitleContainingIgnoreCase(title);
    }
}

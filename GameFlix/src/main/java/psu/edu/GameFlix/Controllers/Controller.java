package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.GameSummary;
import psu.edu.GameFlix.Models.UserSummary;
import psu.edu.GameFlix.Repoitories.GameRepository;
import psu.edu.GameFlix.Repoitories.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173"
})
public class Controller {

  private final UserRepository userRepo;
  private final GameRepository gameRepo;

  public Controller(UserRepository userRepo, GameRepository gameRepo) {
    this.userRepo = userRepo;
    this.gameRepo = gameRepo;
  }

  @GetMapping("/users")
  public List<UserSummary> getUsers() {
    return userRepo.findAllUserSummaries();
  }

   @GetMapping("/games")
   public List<GameSummary> getGames() {
     return gameRepo.findAllGameSummaries();
   }
}

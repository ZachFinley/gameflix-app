package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.User;
import psu.edu.GameFlix.Models.Game;
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
  public List<User> getUsers() {
    return userRepo.findAll();
  }

   @GetMapping("/games")
   public List<Game> getGames() {
     return gameRepo.findAll();
   }
}

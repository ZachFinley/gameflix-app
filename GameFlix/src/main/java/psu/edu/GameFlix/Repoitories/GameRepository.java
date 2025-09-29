package psu.edu.GameFlix.Repoitories;

import org.springframework.data.jpa.repository.JpaRepository;
import psu.edu.GameFlix.Models.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {

}
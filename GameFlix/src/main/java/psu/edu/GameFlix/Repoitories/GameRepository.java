package psu.edu.GameFlix.Repoitories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Models.GameSummary;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT new psu.edu.GameFlix.Models.GameSummary(g.id, g.slug, g.title, g.esrbRating, CAST(g.releaseDate AS string), CAST(g.status AS string), CAST(g.msrp AS double)) FROM Game g")
    List<GameSummary> findAllGameSummaries();
}
package psu.edu.GameFlix.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Repoitories.GameRepository;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository theGameRepository) {
        gameRepository = theGameRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public List<Game> findByTitleContainingIgnoreCase(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }

}

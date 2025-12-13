package psu.edu.GameFlix.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import psu.edu.GameFlix.Models.Game;
import psu.edu.GameFlix.Models.Review;
import psu.edu.GameFlix.Repoitories.GameRepository;
import psu.edu.GameFlix.Repoitories.ReviewRepository;

@Service
public class GameService {

    @PersistenceContext
    private EntityManager entityManager;

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public GameService(GameRepository theGameRepository, ReviewRepository theReviewRepository) {
        gameRepository = theGameRepository;
        reviewRepository = theReviewRepository;
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> searchGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    public Optional<Game> findGameById(int id) {
        return gameRepository.findById(id);
    }

    public List<Review> findGameReviews(int gameId) {
        return reviewRepository.findByGameId(gameId);
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(int id, Review updated) {
        return reviewRepository.findById(id)
                .map(existing -> {
                    existing.setRating(updated.getRating());
                    existing.setTitle(updated.getTitle());
                    existing.setBody(updated.getBody());
                    // keep userId/gameId as originally stored to avoid reassignment
                    return reviewRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public List<Game> findGamesByStatus(String status) {
        TypedQuery<Game> query = entityManager.createQuery(
                "SELECT g FROM Game g WHERE g.status = :status",
                Game.class);
        query.setParameter("status", status.trim());
        return query.getResultList();
    }
}
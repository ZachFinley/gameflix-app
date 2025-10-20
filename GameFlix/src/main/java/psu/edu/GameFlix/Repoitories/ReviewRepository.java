package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByGameId(int gameId);

    List<Review> findByUserId(int userId);

    List<Review> findByRating(int rating);
}

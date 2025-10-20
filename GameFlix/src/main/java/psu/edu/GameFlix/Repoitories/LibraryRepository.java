package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.Library;
import psu.edu.GameFlix.Models.LibraryId;

public interface LibraryRepository extends JpaRepository<Library, LibraryId> {
    List<Library> findByUserId(int userId);

    List<Library> findByGameId(int gameId);
}

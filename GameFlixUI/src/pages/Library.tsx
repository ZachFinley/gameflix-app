import { useEffect, useState } from "react";
import * as api from "../api";
import GameModal from "../components/GameModal";
import RemoveFromLibraryModal from "../components/RemoveFromLibraryModal";
import type { Game, LibraryEntry, Review } from "../types";

type Props = { currentUser: { id: number } | null; allGames: Game[] };

export default function Library({ currentUser, allGames }: Props) {
  const [library, setLibrary] = useState<LibraryEntry[]>([]);
  const [savedGames, setSavedGames] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<Game | null>(null);
  const [gameToRemove, setGameToRemove] = useState<Game | null>(null);
  const [existingReview, setExistingReview] = useState<Review | undefined>(undefined);

  useEffect(() => {
    if (!currentUser) {
      setLibrary([]);
      setSavedGames([]);
      return;
    }
    api.getUserLibrary(currentUser.id)
      .then((list: LibraryEntry[]) => {
        setLibrary(list);
        const sg = list.map((entry: LibraryEntry) => allGames.find(g => g.id === entry.gameId)).filter(Boolean) as Game[];
        setSavedGames(sg);
      })
      .catch((err: unknown) => {
        console.error("Failed to load user library", err);
        setLibrary([]);
        setSavedGames([]);
      });
  }, [currentUser, allGames]);

  useEffect(() => {
    if (!selectedGame || !currentUser) {
      setExistingReview(undefined);
      return;
    }
    // Fetch reviews for this game and find the user's review
    api.getGameReviews(selectedGame.id)
      .then((reviews: Review[]) => {
        const userReview = reviews.find(r => r.userId === currentUser.id);
        setExistingReview(userReview);
      })
      .catch((err: unknown) => {
        console.error("Failed to load reviews", err);
        setExistingReview(undefined);
      });
  }, [selectedGame, currentUser]);

  async function handleAddToLibrary(game: Game) {
    if (!currentUser) return;
    try {
      await api.addToLibrary(currentUser.id, game.id);
      const list = await api.getUserLibrary(currentUser.id);
      setLibrary(list);
      const sg = list.map((entry: LibraryEntry) => allGames.find(g => g.id === entry.gameId)).filter(Boolean) as Game[];
      setSavedGames(sg);
      setSelectedGame(null);
    } catch (err) {
      console.error("Failed to add to library", err);
      const msg = typeof err === "object" && err !== null && "message" in err ? String((err as { message?: unknown }).message) : String(err);
      alert("Failed to add to library: " + msg);
    }
  }

  async function handleRemoveFromLibrary(game: Game) {
    if (!currentUser) return;
    try {
      await api.removeFromLibrary(currentUser.id, game.id);
      const list = await api.getUserLibrary(currentUser.id);
      setLibrary(list);
      const sg = list.map((entry: LibraryEntry) => allGames.find(g => g.id === entry.gameId)).filter(Boolean) as Game[];
      setSavedGames(sg);
      setGameToRemove(null);
    } catch (err) {
      console.error("Failed to remove from library", err);
      const msg = typeof err === "object" && err !== null && "message" in err ? String((err as { message?: unknown }).message) : String(err);
      alert("Failed to remove from library: " + msg);
    }
  }

  return (
    <div className="p-3">
      <h2>Your Library</h2>
      {!currentUser ? (
        <p>Please sign in to view your library.</p>
      ) : savedGames.length === 0 ? (
        <p>Your library is empty.</p>
      ) : (
        <div className="d-flex flex-wrap gap-3">
          {savedGames.map(g => (
            <div key={g.id} className="gf-game-card">
              <img src={g.boxArtUrl} alt={g.title} className="gf-game-image" />
              <div className="fw-semibold">{g.title}</div>
              <div className="text-end mt-2">
                <button className="btn btn-sm btn-danger" onClick={() => setGameToRemove(g)}>Remove</button>
              </div>
            </div>
          ))}
        </div>
      )}

      <hr />
      <h3>Browse Games</h3>
      <div className="d-flex flex-wrap gap-3">
        {allGames.map(g => {
          const inLibrary = library.some(e => e.gameId === g.id);
          return (
            <div key={g.id} className="gf-game-card">
              <img src={`${g.boxArtUrl}`} alt={g.title} className="gf-game-image" />
              <div className="fw-semibold">{g.title}</div>
              <div className="d-flex justify-content-between mt-2">
                <button className="btn btn-sm btn-outline-primary" onClick={() => setSelectedGame(g)}>Details</button>
                <button className="btn btn-sm btn-primary" onClick={() => setSelectedGame(g)} disabled={inLibrary}>{inLibrary ? "Added" : "Add"}</button>
              </div>
            </div>
          );
        })}
      </div>

      {selectedGame && (
        <GameModal 
          game={selectedGame} 
          onClose={() => {
            setSelectedGame(null);
            setExistingReview(undefined);
          }} 
          onAdd={() => handleAddToLibrary(selectedGame)}
          currentUserId={currentUser?.id}
          existingReview={existingReview}
          onSubmitReview={async (review) => {
            if (!currentUser) return;
            try {
              if (existingReview?.id) {
                // Update existing review
                await api.updateReview(existingReview.id, review);
              } else {
                // Add new review
                await api.addReview(review);
              }
              // Fetch the updated reviews
              const reviews = await api.getGameReviews(selectedGame.id);
              const userReview = reviews.find((r: Review) => r.userId === currentUser.id);
              setExistingReview(userReview);
            } catch (err) {
              console.error("Failed to add review", err);
              const msg = typeof err === "object" && err !== null && "message" in err ? String((err as { message?: unknown }).message) : String(err);
              alert("Failed to add review: " + msg);
            }
          }}
        />
      )}

      {gameToRemove && (
        <RemoveFromLibraryModal 
          game={gameToRemove}
          onClose={() => setGameToRemove(null)}
          onConfirm={() => handleRemoveFromLibrary(gameToRemove)}
        />
      )}
    </div>
  );
}

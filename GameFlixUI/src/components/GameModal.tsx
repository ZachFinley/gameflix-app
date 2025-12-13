import { useState } from "react";
import type { Game, Review } from "../types";
import ReviewModal from "./ReviewModal";

type Props = {
  game: Game;
  onClose: () => void;
  onAdd?: () => void;
  addLabel?: string;
  currentUserId?: number;
  existingReview?: Review;
  onSubmitReview?: (review: Omit<Review, "id" | "createdAt" | "updatedAt">) => void;
};

export default function GameModal({ game, onClose, onAdd, addLabel = "Add to Library", currentUserId, existingReview, onSubmitReview }: Props) {
  const [showReview, setShowReview] = useState(false);

  return (
    <div className="gf-modal-backdrop">
      <div className="gf-modal">
        <h3>{game.title}</h3>
        <p><strong>Status:</strong> {game.status || "-"}</p>
        <p>{game.description || "No description"}</p>
        <div className="gf-actions justify-content-end mt-2">
          <button className="btn btn-secondary" onClick={onClose}>Close</button>
          {onAdd && <button className="btn btn-success" onClick={onAdd}>{addLabel}</button>}
          {currentUserId && onSubmitReview && (
            <button className="btn btn-outline-primary" onClick={() => setShowReview(true)}>
              {existingReview ? "Edit Review" : "Review"}
            </button>
          )}
        </div>
      </div>

      {showReview && currentUserId && onSubmitReview && (
        <ReviewModal
          gameId={game.id}
          userId={currentUserId}
          existingReview={existingReview}
          onSubmit={(review: Omit<Review, "id" | "createdAt" | "updatedAt">) => {
            onSubmitReview(review);
            setShowReview(false);
          }}
          onClose={() => setShowReview(false)}
        />
      )}
    </div>
  );
}

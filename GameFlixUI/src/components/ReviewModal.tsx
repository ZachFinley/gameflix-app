import React, { useState } from 'react';
import type { Review } from "../types";

type Props = {
    gameId: number;
    userId: number;
    existingReview?: Review;
    onSubmit: (review: Omit<Review, 'id' | 'createdAt' | 'updatedAt'>) => void;
    onClose: () => void;
}

const ReviewModal = ({
    gameId,
    userId,
    existingReview,
    onSubmit,
    onClose,
}: Props) => {
    const [rating, setRating] = useState(existingReview?.rating || 5);
    const [title, setTitle] = useState(existingReview?.title || '');
    const [body, setBody] = useState(existingReview?.body || '');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        onSubmit({
            userId,
            gameId,
            rating,
            title: title.trim(),
            body: body.trim(),
        });

        onClose();
    };

    if (existingReview && !existingReview.id) {
        return (
            <div className="gf-review-modal-backdrop" onClick={onClose}>
                <div className="gf-review-modal" onClick={(e) => e.stopPropagation()}>
                    <h2>You already reviewed this game</h2>
                    <p className="text-muted">You can only submit one review per game.</p>
                    <button className="btn btn-secondary" onClick={onClose}>Close</button>
                </div>
            </div>
        );
    }

    return (
        <div className="gf-review-modal-backdrop" onClick={onClose}>
            <div className="gf-review-modal" onClick={(e) => e.stopPropagation()}>
                <button className="btn btn-close" onClick={onClose} aria-label="Close modal"></button>
                <h2>{existingReview ? 'Edit Review' : 'Write a Review'}</h2>
                
                <form onSubmit={handleSubmit} className="gf-review-form">
                    <div className="gf-form-group">
                        <label htmlFor="rating">Rating</label>
                        <select id="rating" value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                            {[1, 2, 3, 4, 5].map((star) => (
                                <option key={star} value={star}>
                                    {star}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="gf-form-group">
                        <label htmlFor="title">Title</label>
                        <input
                            id="title"
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            placeholder="Summary of your review"
                            maxLength={100}
                        />
                    </div>

                    <div className="gf-form-group">
                        <label htmlFor="body">Comment</label>
                        <textarea
                            id="body"
                            value={body}
                            onChange={(e) => setBody(e.target.value)}
                            placeholder="Share your thoughts about this game"
                            maxLength={1000}
                        />
                    </div>

                    <button type="button" onClick={onClose} className="btn btn-secondary">
                        Cancel
                    </button>
                    <button type="submit" className="btn btn-primary">
                        {existingReview ? 'Update Review' : 'Submit Review'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ReviewModal;
import type { Game } from "../types";

type Props = {
  game: Game;
  onClose: () => void;
  onConfirm: () => void;
};

export default function RemoveFromLibraryModal({ game, onClose, onConfirm }: Props) {
    return (
        <div className="gf-modal-backdrop">
            <div className="gf-modal">
                <h3>Remove from Library</h3>
                <p>Are you sure you want to remove <strong>{game.title}</strong> from your library?</p>
                <div className="gf-actions justify-content-end">
                    <button className="btn btn-secondary" onClick={onClose}>Cancel</button>
                    <button className="btn btn-danger" onClick={onConfirm}>Remove</button>
                </div>
            </div>
        </div>
    );
}
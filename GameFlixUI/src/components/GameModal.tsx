type Game = { id: number; title: string; status?: string; image?: string; description?: string };

type Props = {
  game: Game;
  onClose: () => void;
  onAdd?: () => void;
  addLabel?: string;
};

export default function GameModal({ game, onClose, onAdd, addLabel = "Add to Library" }: Props) {
  return (
    <div className="gf-modal-backdrop">
      <div className="gf-modal">
        <h3>{game.title}</h3>
        <p><strong>Status:</strong> {game.status || "-"}</p>
        <p>{game.description || "No description"}</p>
        <div className="gf-actions justify-content-end mt-2">
          <button className="btn btn-secondary" onClick={onClose}>Close</button>
          {onAdd && <button className="btn btn-success" onClick={onAdd}>{addLabel}</button>}
        </div>
      </div>
    </div>
  );
}

type Props = {
  title?: string;
  onConfirm: () => void;
  onCancel: () => void;
};

export default function DeleteModal({ title = "this item", onConfirm, onCancel }: Props) {
  return (
    <div className="gf-modal-backdrop">
      <div className="gf-modal">
        <h3>Confirm Delete</h3>
        <p>Are you sure you want to delete {title}?</p>
        <div className="gf-actions justify-content-end">
          <button className="btn btn-secondary" onClick={onCancel}>Cancel</button>
          <button className="btn btn-danger" onClick={onConfirm}>Delete</button>
        </div>
      </div>
    </div>
  );
}

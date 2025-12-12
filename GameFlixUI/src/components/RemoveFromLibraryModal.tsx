export default function RemoveFromLibraryModal() {
    return (
        <div className="gf-modal-backdrop">
            <div className="gf-modal">
                <h3>Remove from Library</h3>
                <p>Are you sure you want to remove this game from your library?</p>
                <div className="gf-actions justify-content-end">
                    <button className="btn btn-secondary">Cancel</button>
                    <button className="btn btn-danger">Remove</button>
                </div>
            </div>
        </div>
    );
}
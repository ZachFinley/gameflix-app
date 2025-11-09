type Game = { id: number; title: string; slug?: string; status?: string };

type Props = {
  games: Game[];
  onEdit: (g: Game) => void;
  onDelete: (g: Game) => void;
  onAdd?: () => void;
};

export default function Admin({ games, onEdit, onDelete, onAdd }: Props) {
  return (
    <div className="p-3">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Admin — Manage Games</h2>
        <div>
          <button className="btn btn-primary" onClick={() => onAdd && onAdd()}>Add Game</button>
        </div>
      </div>
      <table className="table gf-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Slug</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {games.map(g => (
            <tr key={g.id}>
              <td>{g.id}</td>
              <td>{g.title}</td>
              <td>{g.slug || "-"}</td>
              <td>{g.status || "-"}</td>
              <td>
                <button className="btn btn-sm btn-outline-secondary me-2" onClick={() => onEdit(g)}>Update</button>
                <button className="btn btn-sm btn-danger" onClick={() => onDelete(g)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

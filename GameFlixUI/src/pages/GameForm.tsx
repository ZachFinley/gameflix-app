import { useState } from "react";

type Game = {
  id?: number;
  title?: string;
  slug?: string;
  esrbRating?: string;
  releaseDate?: string;
  status?: string;
  description?: string;
  imageUrl?: string;
};

type Props = {
  game?: Game;
  onCancel?: () => void;
  onSave?: (g: Game) => void;
};

export default function GameForm({ game, onCancel, onSave }: Props) {
  const [form, setForm] = useState<Game>({ ...(game || {}) });

  function change<K extends keyof Game>(k: K, v: Game[K]) {
    setForm(prev => ({ ...prev, [k]: v }));
  }

  function submit(e: React.FormEvent) {
    e.preventDefault();
    console.log("save", form);
    onSave?.(form);
  }

  return (
    <div className="p-3">
      <h2>{game ? "Edit Game" : "Add Game"}</h2>
  <form onSubmit={submit} className="d-grid gap-2 gf-form-max-md">
        <label className="form-label">
          Title
          <input className="form-control" value={form.title || ""} onChange={e => change("title", e.target.value)} />
        </label>
        <label className="form-label">
          Slug
          <input className="form-control" value={form.slug || ""} onChange={e => change("slug", e.target.value)} />
        </label>
        <label className="form-label">
          ESRB
          <input className="form-control" value={form.esrbRating || ""} onChange={e => change("esrbRating", e.target.value)} />
        </label>
        <label className="form-label">
          Release Date
          <input className="form-control" type="date" value={form.releaseDate || ""} onChange={e => change("releaseDate", e.target.value)} />
        </label>
        <label className="form-label">
          Status
          <input className="form-control" value={form.status || ""} onChange={e => change("status", e.target.value)} />
        </label>
        <label className="form-label">
          Description
          <textarea className="form-control" value={form.description || ""} onChange={e => change("description", e.target.value)} />
        </label>
        <div className="d-flex gap-2">
          <button className="btn btn-primary" type="submit">Save</button>
          <button className="btn btn-outline-secondary" type="button" onClick={onCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
}

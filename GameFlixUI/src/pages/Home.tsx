import { useEffect, useState } from "react";
import * as api from "../api";
import GameModal from "../components/GameModal";
import type { Game } from "../types";


export default function Home() {
  const [newly, setNewly] = useState<Game[]>([]);
  const [other, setOther] = useState<Game[]>([]);
  const [upcoming, setUpcoming] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<Game | null>(null);

  useEffect(() => {
    api.getAllGames()
      .then((games: Game[]) => {
        const now = new Date();
        const threeMonthsAgo = new Date(now.getFullYear(), now.getMonth() - 3, now.getDate());

        setUpcoming(games.filter(g => g.releaseDate && new Date(g.releaseDate) > now));
        const released = games.filter(g => {
          if (!g.releaseDate) return false;
          const releaseDate = new Date(g.releaseDate);
          return releaseDate <= now && releaseDate >= threeMonthsAgo;
        });
        const others = games.filter(g => g.releaseDate && new Date(g.releaseDate) < threeMonthsAgo);
        setNewly(released);
        setOther(others);
      })
      .catch(err => console.error("Failed to load games for home", err));
  }, []);

  function renderSection(title: string, items: Game[]) {
    return (
      <section className="mb-3">
        <h3 className="mt-3">{title}</h3>
        <div className="d-flex flex-wrap gap-3">
          {items.map(g => (
            <div key={g.id} className="gf-game-card">
              <img src={g.boxArtUrl} alt={g.title} className="gf-game-image" />
              <div className="fw-semibold">{g.title}</div>
              <div className="text-end mt-2">
                <button className="btn btn-sm btn-outline-primary" onClick={() => setSelectedGame(g)}>Info</button>
              </div>
            </div>
          ))}
        </div>
      </section>
    );
  }

  return (
    <div className="p-3">
      <h2>Home</h2>
      {renderSection("Newly Released", newly)}
      {renderSection("Other Games", other)}
      {renderSection("Upcoming", upcoming)}

      {selectedGame && (
        <GameModal game={selectedGame} onClose={() => setSelectedGame(null)} />
      )}
    </div>
  );
}

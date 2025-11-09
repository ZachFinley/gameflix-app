import { useEffect, useState } from "react";
import * as api from "../api";

type Game = { id: number; title: string; status?: string; image?: string };
type ApiGame = { id: number; title?: string; status?: string; imageUrl?: string };

export default function Home() {
  const [newly, setNewly] = useState<Game[]>([]);
  const [other, setOther] = useState<Game[]>([]);
  const [upcoming, setUpcoming] = useState<Game[]>([]);

  useEffect(() => {
    api.getAllGames()
      .then((list: ApiGame[]) => {
        const games: Game[] = list.map((g: ApiGame) => ({ id: g.id, title: g.title || "", status: g.status, image: g.imageUrl }));
        setUpcoming(games.filter(g => (g.status || "").toLowerCase() === "upcoming"));
        const released = games.filter(g => (g.status || "").toLowerCase() === "released");
        const others = games.filter(g => !["released", "upcoming"].includes((g.status || "").toLowerCase()));
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
              <div className="gf-game-image">[image]</div>
              <div className="fw-semibold">{g.title}</div>
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
    </div>
  );
}

import { useState, useEffect } from "react";
import Navbar from "./components/Navbar";
import AuthArea from "./components/AuthArea";
import Home from "./pages/Home";
import Library from "./pages/Library";
import Admin from "./pages/Admin";
import GameForm from "./pages/GameForm";
import UserModal from "./components/UserModal";
import DeleteModal from "./components/DeleteModal";
import * as api from "./api";
import type { User, Game } from "./types";

export default function App() {
  const [page, setPage] = useState<string>("home");
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [showUserModal, setShowUserModal] = useState(false);
  const [showDelete, setShowDelete] = useState<{ open: boolean; game?: Game }>({ open: false });
  const [editingGame, setEditingGame] = useState<Game | null>(null);

  const [games, setGames] = useState<Game[]>([]);

  useEffect(() => {
    api.getAllGames()
      .then((g: Game[]) => setGames(g))
      .catch(err => console.error("Failed to load games", err));
  }, []);

  function navigate(p: string) {
    setPage(p);
  }

  function openAdd() {
    setEditingGame(null);
    setPage("add");
  }

  function handleEdit(g: Game) {
    setEditingGame(g);
    setPage("edit");
  }

  function handleDelete(g: Game) {
    setShowDelete({ open: true, game: g });
  }

  function confirmDelete() {
    if (showDelete.game) {
      setGames(prev => prev.filter(x => x.id !== showDelete.game!.id));
    }
    setShowDelete({ open: false });
  }

  function saveGame(g: Partial<Game>) {
    if (g.id) {
      setGames(prev => prev.map(x => (x.id === g.id ? { ...x, ...g } : x)));
    } else {
      const id = Math.max(0, ...games.map(x => x.id)) + 1;
  setGames(prev => [...prev, { id, title: g.title ?? "Untitled", slug: g.slug ?? "", status: g.status ?? "" }]);
    }
    setPage("admin");
  }

  return (
  <div className="gf-app container-fluid">
      <Navbar currentUser={currentUser} onNavigate={navigate} onOpenUserModal={() => setShowUserModal(true)} />

      <main>
        {page === "home" && <Home />}
        {page === "library" && <Library currentUser={currentUser} allGames={games} />}
        {page === "admin" && <Admin games={games} onEdit={handleEdit} onDelete={handleDelete} onAdd={openAdd} />}
        {page === "add" && <GameForm onCancel={() => setPage("admin")} onSave={saveGame} />}
        {page === "edit" && editingGame && <GameForm game={editingGame} onCancel={() => setPage("admin")} onSave={saveGame} />}
        {page === "signin" && (
          <AuthArea onSignedIn={(u: User) => { setCurrentUser(u); setPage("home"); }} />
        )}
      </main>

      {showUserModal && currentUser && (
        <UserModal
          user={currentUser}
          onClose={() => setShowUserModal(false)}
          onSignOut={() => { setCurrentUser(null); setPage("home"); }}
        />
      )}
      {showDelete.open && <DeleteModal title={showDelete.game?.title} onConfirm={confirmDelete} onCancel={() => setShowDelete({ open: false })} />}
    </div>
  );
}

import { useEffect, useState } from "react";

const API_BASE = "http://localhost:8080/api";

type User = {
  id: number;
  email: string;
  displayName: string;
  emailVerified: boolean;
  admin: boolean;
  createdAt?: string;
};

function UsersTable() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${API_BASE}/users`)
      .then(r => r.json())
      .then(setUsers)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading users…</p>;
  if (!users.length) return <p>No users found.</p>;

  return (
    <div>
      <h2>Users</h2>
      <table cellPadding="6">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Display Name</th>
            <th>Email Verified</th>
            <th>Admin</th>
            <th>Created</th>
          </tr>
        </thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.email}</td>
              <td>{u.displayName}</td>
              <td>{u.emailVerified ? "Yes" : "No"}</td>
              <td>{u.admin ? "Yes" : "No"}</td>
              <td>{u.createdAt?.replace("T", " ")}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

type Game = {
  id: number;
  slug: string;
  title: string;
  esrbRating?: string;
  releaseDate?: string;
  status: string;
  msrp?: number;
};

function GamesTable() {
  const [games, setGames] = useState<Game[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${API_BASE}/games`)
      .then(r => r.json())
      .then(setGames)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading games…</p>;
  if (!games.length) return <p>No games found.</p>;

  return (
    <div style={{ marginTop: 24 }}>
      <h2>Games</h2>
      <table cellPadding="6">
        <thead>
          <tr>
            <th>ID</th>
            <th>Slug</th>
            <th>Title</th>
            <th>ESRB</th>
            <th>Release Date</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {games.map(g => (
            <tr key={g.id}>
              <td>{g.id}</td>
              <td>{g.slug}</td>
              <td>{g.title}</td>
              <td>{g.esrbRating || "-"}</td>
              <td>{g.releaseDate || "-"}</td>
              <td>{g.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default function App() {
  return (
    <main style={{ padding: 24, fontFamily: "sans-serif" }}>
      <UsersTable />
      <GamesTable />
    </main>
  );
}
